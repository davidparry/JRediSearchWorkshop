package com.davidparry.services;

import com.davidparry.example.BookDemo;
import com.davidparry.model.AjaxAutoComplete;
import com.davidparry.model.AjaxResult;
import com.davidparry.model.AjaxResults;
import com.davidparry.model.Suggestions;
import io.redisearch.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    Logger logger = LoggerFactory.getLogger(SearchService.class);
    @Autowired
    private BookDemo bookDemo;

    public AjaxResults findByTerm(String term) {
        logger.info("Searching for term " + term);
        SearchResult searchResult = bookDemo.search(term);
        List<AjaxResult> results = searchResult.docs.stream().map(result ->
                AjaxResult.newBuilder().withChapter((String) result.get("chapter")).withLine((String) result.get("line"))
                        .withText((String) result.get("text")).withTitle((String) result.get("title"))
                        .withScore(result.getScore()).withId(result.getId()).build()
        ).collect(Collectors.toList());
        return new AjaxResults(searchResult.totalResults, results);
    }

    public Suggestions suggestTerm(String partialTerm) {
        return new Suggestions(bookDemo.getSuggestions(partialTerm).stream().map(suggestion ->
                new AjaxAutoComplete(suggestion.getString(), suggestion.getScore() + "")
        ).collect(Collectors.toList()));
    }

    @PostConstruct
    private void iniDataForTesting() {
        logger.info("Indexing the book");
        bookDemo.createSearchableIndexBook();
        logger.info("Priming the suggestions");
        bookDemo.primeSuggestions();
    }

}
