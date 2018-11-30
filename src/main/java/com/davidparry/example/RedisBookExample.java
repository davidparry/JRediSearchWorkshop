package com.davidparry.example;

import io.redisearch.Schema;
import io.redisearch.client.Client;

public class RedisBookExample extends RedisBase {

    public boolean createSchema() {
        Schema schema = new Schema().addTextField("book", 1.0)
                .addTextField("text", 1.0)
                .addTextField("description", 1.0);
        getIndexClient().dropIndex(true);

        return getIndexClient().createIndex(schema, Client.IndexOptions.Default());

    }
}