package com.credits.wallet.web;

import com.credits.leveldb.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public class AbstractController {

    @Value("${api.server.host}")
    private String apiServerHost;

    @Value("${api.server.port}")
    private Integer apiServerPort;

    protected ApiClient apiClient;

    protected static final String ERROR_CODE = "REST error";


    @PostConstruct
    private void init() {
        this.apiClient = ApiClient.getInstance(apiServerHost, apiServerPort);
    }

}
