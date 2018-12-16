package com.davidparry.example;

import com.davidparry.example.util.Book;
import io.redisearch.Client;
import io.redisearch.Document;
import io.redisearch.Query;
import io.redisearch.Schema;
import io.redisearch.SearchResult;
import io.redisearch.Suggestion;
import io.redisearch.client.AddOptions;
import io.redisearch.client.SuggestionOptions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookDemo {
    private Client client = new io.redisearch.client.Client("art_book", "localhost", 6379);
    private Schema schema = new Schema().addTextField("id", 0.05)
            .addTextField("text", 0.5)
            .addNumericField("chapter")
            .addNumericField("line")
            .addTextField("title", 1.0);


    /**
     * We create our searchable index from the text from the line
     */
    public void createSearchableIndexBook() {
        // clean up from other examples but leave it for the rest of the example
        client.dropIndex(true);
        client.createIndex(schema, io.redisearch.client.Client.IndexOptions.Default());
        createDocuments().forEach(doc -> {
            client.addDocument(doc, new AddOptions());
        });
    }

    /**
     * We do a search that returns us the scores
     *
     * @param term what to search for
     * @return the result of our search
     */
    public SearchResult search(String term) {
        Query query = new Query(term).setWithScores();
        return client.search(query);
    }

    /**
     * Going to search limit to the first 50 pages
     *
     * @param term word to search for
     * @return the result sorted
     */
    public SearchResult searchFirstLines(String term, int lines) {
        Query query = new Query(term).setWithScores().limit(0, 100)
                .addFilter(new Query.NumericFilter("line", 0, lines));
        return client.search(query);
    }

    /**
     * Load suggestions after 2 characters ask for suggestion to fill to allow user to pick
     */
    public void primeSuggestions() {
        createSuggestionSet().forEach(suggestion -> {
            client.addSuggestion(suggestion, false);
        });
    }

    public List<Suggestion> getSuggestions(String partial) {
        List<Suggestion> suggestions = client.getSuggestion(partial, SuggestionOptions.builder().fuzzy().build());
        suggestions.stream().map(suggestion ->
                suggestion.getString()
        ).collect(Collectors.toList());

        return suggestions;
    }


    private List<Document> createDocuments() {
        final List<Document> documents = new ArrayList<>();
        Book.INSTANCE.getLines().forEach(line -> {
            Map<String, Object> fields = new HashMap<>();
            fields.put("text", line.getText());
            fields.put("chapter", line.getChapter());
            fields.put("line", line.getLine());
            fields.put("title", "title of tbe book");
            documents.add(new Document(line.getId(), fields));
        });
        return documents;
    }

    /**
     * Cheap way not efficient algorithm to load the unique set of terms
     *
     * @return
     */
    private Set<Suggestion> createSuggestionSet() {
        final Set<Suggestion> terms = new HashSet<>();
        Book.INSTANCE.getLines().forEach(line -> {
            String[] values = StringUtils.split(line.getText());
            for (int v = 0; v < values.length; v++) {
                // cleanse from things like colons
                if (StringUtils.isAlpha(values[v])) {
                    terms.add(Suggestion.builder().str(values[v]).score(0.5).build());
                }
            }
        });
        return terms;
    }


}
