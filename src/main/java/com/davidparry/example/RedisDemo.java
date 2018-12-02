package com.davidparry.example;

import io.redisearch.Client;

public class RedisDemo {

    /**
     * For convience used to point to localhost, port and the index called book
     * Note this class does not test out that it can connect to Redis, it only sets up the objects to use.
     * @return client that is ready to make calls to Redis
     */
    public Client getClient() {
        return new io.redisearch.client.Client("book", "localhost",6379);
    }


    /**
     * If you do not already have an Index then the only way to tell if your connection is valid is to send the
     * dropindex call to redis with flag true. This actually will throw an exception but the api catches it if you
     * send in a true flag. If connection is not able to establish still get an exception.
     *
     * Command: FT.DROP
     *
     * @return true if connection was successful
     */
    public boolean checkConnection() {
       return !getClient().dropIndex(true);
    }


}
