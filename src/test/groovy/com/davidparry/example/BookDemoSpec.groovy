package com.davidparry.example

import io.redisearch.SearchResult
import org.apache.commons.lang3.StringUtils
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class BookDemoSpec extends Specification {

    @Shared
    BookDemo demo = new BookDemo()

    def setup() {
        demo.createSearchableIndexBook()
    }


    @Unroll
    def "search '#term' top score #score total #total"() {

        when:
        SearchResult result = demo.search(term)

        then:
        result.docs.get(0).score == score
        result.totalResults == total

        where:
        term     | score | total
        "stone"  | 7.5   | 75
        "nature" | 4.0   | 80
    }

    @Unroll
    def "Partial word '#partial' index #index total hits #total"() {
        given:
        demo.primeSuggestions()

        when:
        List<String> suggestion = demo.getSuggestions(partial)

        then:
        String term = suggestion.get(index)
        SearchResult result = demo.search(term)
        result.totalResults == total
        // our search is not case sensitive here is the proof
        StringUtils.containsIgnoreCase(result.docs.get(0).get("text"), term)

        where:
        partial | total | index
        "st"    | 86    | 0
        "st"    | 2     | 1
        "st"    | 75    | 2
        "st"    | 75    | 3
        "st"    | 2     | 4
    }

    @Unroll
    def "For term '#term' searching from line 1 to #line"() {

        when:
        SearchResult searchResult = demo.searchFirstLines(term, line)

        then:
        StringUtils.containsIgnoreCase((String) searchResult.docs.get(0).get("text")
                + (String) searchResult.docs.get(0).get("title"), term)
        searchResult.totalResults == total

        where:
        term    | line | total
        "stone" | 20   | 4
        "stone" | 50   | 11
        "stone" | 10   | 1


    }


}
