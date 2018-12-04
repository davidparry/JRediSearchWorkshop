package com.davidparry.example;

import com.google.gson.Gson;
import io.redisearch.Client;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.Schema;
import io.redisearch.SearchResult;
import io.redisearch.client.AddOptions;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * A simple document being added to the index through the Client Interface
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/Client.html#addDocument-io.redisearch.Document-io.redisearch.client.AddOptions-
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftadd
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftinfo field "num_docs" the amount of docs in the index
     * <p>
     * Command FT.ADD
     *
     * @return the number of documents should only be 1 for this example
     */
    public int addSimpleBookDocument() {
        createSchema();
        Map<String, Object> fields = new HashMap<>();
        fields.put("text", "I am an example sentence.");
        fields.put("chapter", 1);
        fields.put("line", 1);
        fields.put("title", "title of tbe book");
        getClient().addDocument(new Document("123-abc", fields), new AddOptions());
        Map<String, Object> info = getClient().getInfo();
        return Integer.parseInt((String) info.get("num_docs"));
    }


    /**
     * Show how to add a payload to the document. Currently the module will not retrieve do to the bug in JRedisearch.
     * Though you can add the payload you can not the Query object not using the correct argument WITHPAYLOADS
     *
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/Client.html#addDocument-io.redisearch.Document-io.redisearch.client.AddOptions-
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/Document.html#Document-java.lang.String-java.util.Map-double-byte:A-
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftadd
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftinfo field "num_docs" the amount of docs in the index
     * <p>
     * <p>
     * Command FT.ADD
     *
     * @return number of records should be 1 for this example
     */
    public int addDocumentwithPayload() {
        createSchema();
        Map<String, Object> fields = new HashMap<>();
        fields.put("text", "A sentence in the book.");
        fields.put("chapter", 3);
        fields.put("line", 56);
        fields.put("title", "title of tbe book is");
        fields.put("large", "i am more data that really do not want to be indexed but stored in redis as a payload");
        Gson gson = new Gson();

        getClient().addDocument(new Document("123-abc", fields, 1.0, gson.toJson(fields).getBytes()), new AddOptions());
        Map<String, Object> info = getClient().getInfo();
        return Integer.parseInt((String) info.get("num_docs"));
    }

    /**
     * A simple example of adding 2 Documents have them indexed and then do a simple search for term book
     * <p>
     * https://oss.redislabs.com/redisearch/Commands/#ftsearch
     * <p>
     * http://davidparry.com/storage/jrediseach-javadoc-v0-19-0/docs/io/redisearch/Client.html#search-io.redisearch.Query-
     * <p>
     * <p>
     * Command FT.SEARCH
     *
     * @return number of documents that have book should be 1
     */
    public long countNumberOfDocumentsHaveBookTerm() {
        createSchema();
        Map<String, Object> fields = new HashMap<>();
        fields.put("text", "I am some text that is on one line of the book.");
        fields.put("chapter", 23);
        fields.put("line", 300);
        fields.put("title", "I am a title of the book");
        getClient().addDocument(new Document("123", fields), new AddOptions());
        fields.put("title", "I am a title but something missing");
        fields.put("line", 301);
        fields.put("text", "another line of text that will not have the same term we will be searching on.");
        getClient().addDocument(new Document("124", fields), new AddOptions());

        SearchResult searchResult = getClient().search(new Query("book"));
        Map<String, Object> info = getClient().getInfo();
        // see 2 documents arr in the index
        assert 2 == Integer.parseInt((String) info.get("num_docs"));

        return searchResult.totalResults;
    }


}
