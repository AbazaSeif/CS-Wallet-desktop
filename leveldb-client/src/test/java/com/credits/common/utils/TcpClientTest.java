package com.credits.common.utils;

import com.credits.common.exception.CreditsException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClientTest.class);

    @Ignore
    @Test
    public void sendRequestTest1() {
        String walletBASE64 = "KoX94CTSk7v7b28dzN7hXNvYPn8wxn7cStYfY3F7hMn";
        String serverHostname = "localhost";
        int serverPortNumber = 20000;
        try {
            byte[] walletBytes = Converter.decodeFromBASE64(walletBASE64);
            String walletBASE58 = Converter.encodeToBASE58(walletBytes);
            String response = TcpClient.sendRequest(serverHostname, serverPortNumber, walletBASE58);
            LOGGER.info("response = {}", response);
        } catch (CreditsException e) {
            LOGGER.error(e.getMessage());
            assert false;
        }
    }

    @Ignore
    @Test
    public void sendRequestTest2() {
        String walletBASE58 = "KoX94CTSk7v7b28dzN7hXNvYPn8wxn7cStYfY3F7hMn";
        String serverHostname = "localhost";
        int serverPortNumber = 20000;
        try {
            String response = TcpClient.sendRequest(serverHostname, serverPortNumber, walletBASE58);
            LOGGER.info("response = {}", response);
        } catch (CreditsException e) {
            LOGGER.error(e.getMessage());
            assert false;
        }
    }
}
