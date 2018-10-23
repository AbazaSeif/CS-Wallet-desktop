package com.credits.leveldb.client.pool;

import org.apache.commons.pool.*;
import org.apache.commons.pool.impl.*;
import org.apache.thrift.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.*;
import org.slf4j.*;

public class ThriftClientPool<T extends TServiceClient> implements
    AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientPool.class);

    private final GenericObjectPool<T> internalPool;

    public ThriftClientPool(ClientFactory<T> clientFactory,
        GenericObjectPool.Config poolConfig, String host, int port) {
        this(clientFactory, new BinaryOverSocketProtocolFactory(host, port),
            poolConfig);
    }

    public ThriftClientPool(ClientFactory<T> clientFactory,
        ProtocolFactory protocolFactory, GenericObjectPool.Config poolConfig) {
        this.internalPool = new GenericObjectPool<T>(new ThriftClientFactory(
            clientFactory, protocolFactory), poolConfig);
    }

    class ThriftClientFactory extends BasePoolableObjectFactory<T> {

        private ClientFactory<T> clientFactory;
        private ProtocolFactory protocolFactory;

        public ThriftClientFactory(ClientFactory<T> clientFactory,
            ProtocolFactory protocolFactory) {
            this.clientFactory = clientFactory;
            this.protocolFactory = protocolFactory;
        }

        @Override
        public T makeObject() {
            try {
                TProtocol protocol = protocolFactory.make();
                return clientFactory.make(protocol);
            } catch (Exception e) {
                LOGGER.warn("whut?", e);
                throw new ThriftClientException(
                    "Can not make a new object for pool", e);
            }
        }

        @Override
        public void destroyObject(T obj) {
            if (obj.getOutputProtocol().getTransport().isOpen()) {
                obj.getOutputProtocol().getTransport().close();
            }
            if (obj.getInputProtocol().getTransport().isOpen()) {
                obj.getInputProtocol().getTransport().close();
            }
        }
    }

    public static interface ClientFactory<T> {

        T make(TProtocol tProtocol);
    }

    public static interface ProtocolFactory {

        TProtocol make();
    }

    public static class BinaryOverSocketProtocolFactory implements
        ProtocolFactory {

        private String host;
        private int port;

        public BinaryOverSocketProtocolFactory(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public TProtocol make() {
            TTransport transport = new TSocket(host, port,30000);
            try {
                transport.open();
            } catch (TTransportException e) {
                LOGGER.warn("whut?", e);
                throw new ThriftClientException("Can not make protocol", e);
            }
            return new TBinaryProtocol(transport);
        }
    }

    public static class ThriftClientException extends RuntimeException {

        public ThriftClientException(String message, Exception e) {
            super(message, e);
        }

    }

    public T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new ThriftClientException(
                "Could not get a resource from the pool", e);
        }
    }

    public void returnResourceObject(T resource) {
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new ThriftClientException(
                "Could not return the resource to the pool", e);
        }
    }

    public void returnBrokenResource(T resource) {
        returnBrokenResourceObject(resource);
    }

    public void returnResource(T resource) {
        returnResourceObject(resource);
    }

    protected void returnBrokenResourceObject(T resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new ThriftClientException(
                "Could not return the resource to the pool", e);
        }
    }

    public void destroy() {
        close();
    }

    public void close() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new ThriftClientException("Could not destroy the pool", e);
        }
    }

    public Integer getNumActive() {
        return internalPool.getNumActive();
    }

    public Integer getNumIdle() {
        return internalPool.getNumIdle();
    }
}