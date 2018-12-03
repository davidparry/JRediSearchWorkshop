package com.davidparry.example;

import io.redisearch.Client;
import io.redisearch.Schema;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.List;

/**
 * Link to the JRedis client and the javadoc to reference
 * https://oss.redislabs.com/redisearch/java_client/
 * <p>
 * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/index.html?index-all.html
 */
public class RedisDemo {

    /**
     * For convience used to point to localhost, port and the index called book
     * Note this class does not test out that it can connect to Redis, it only sets up the objects to use.
     * <p>
     * The link to the client https://oss.redislabs.com/redisearch/java_client/
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/client/Client.html
     *
     * @return client that is ready to make calls to Redis
     */
    public Client getClient() {
        return new io.redisearch.client.Client("book", "localhost", 6379);
    }


    /**
     * No ideal way to check for a connection when index does not exist
     * But this is another way which does the least amount of harm but still has problems.
     * See post http://davidparry.com/blog/2018/12/2/testing-conductivity-in-jredissearch-to-redis-without-a-prev.html
     *
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/client/Client.html#getInfo--
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftinfo
     * <p>
     * Command FT.INFO
     *
     * @return true if connection was successful
     */
    public boolean checkConnection() {
        boolean flag = true;
        try {
            getClient().getInfo();
        } catch (JedisConnectionException je) {
            flag = false;
        } catch (JedisDataException jex) {
            // index not present or some other data exception :-(
        }
        return flag;
    }

    /**
     * Create a basic Schema adding the weight we want to influence the indexing algorithm when adding a document.
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/client/Client.html#createIndex-io.redisearch.Schema-io.redisearch.client.Client.IndexOptions-
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftcreate
     * <p>
     * Command: FT.CREATE
     *
     * @return true if it was successful in creating a new index. The index is the name used when creating the client.
     */
    public boolean createSchema() {
        Schema schema = new Schema().addTextField("id", 0.05)
                .addTextField("text", 0.5)
                .addNumericField("chapter")
                .addNumericField("line")
                .addTextField("title", 1.0);
        return getClient().createIndex(schema, io.redisearch.client.Client.IndexOptions.Default());
    }

    /**
     * Check the name of our index that is used in the initial Client creation is the same.
     * Check the first field in the info of the schema for the index is id
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/client/Client.html#getInfo--
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftinfo
     * <p>
     * Command FT.INFO
     *
     * @return true if we are satisfied that its our Index
     */
    public boolean validateIndexSchema() {
        List b = (List) getClient().getInfo().get("fields");
        assert StringUtils.equals(new String((byte[]) ((List) b.get(0)).get(0)), "id");
        return StringUtils.equals((CharSequence) getClient().getInfo().get("index_name"), "book");
    }

}
