package com.davidparry.example;

import io.redisearch.client.Client;

public abstract class RedisBase {

    private String host = "localhost";
    private int port = 6379;
    private String index = "book";

    public io.redisearch.Client getIndexClient() {
        return new Client(this.index, host, port);
    }


}
