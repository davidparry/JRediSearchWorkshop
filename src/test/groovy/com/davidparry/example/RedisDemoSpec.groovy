package com.davidparry.example

import spock.lang.Shared
import spock.lang.Specification

class RedisDemoSpec extends Specification {

    @Shared
    RedisDemo demo = new RedisDemo()

    def setup() {
        demo.getClient().dropIndex(true)
    }


    def "do we have a client "() {

        expect:
        demo.getClient() != null

    }

    def "well what about a valid connection"() {

        expect:
        demo.checkConnection()
    }

    def "create the index " () {

        expect:
        demo.createSchema()
        // check that our index is same as what we passed to the connection
        demo.getClient().getInfo().get("index_name") == "book"
    }

    def "Lets validate our Index and Schema" () {

        expect:
        demo.createSchema()
        demo.validateIndexSchema()
    }

    def "Add a simple document to the index"() {

        expect:
        demo.addSimpleBookDocument() == 1

    }

    def "Add a document with a payload to the index"() {

        expect:
        demo.addDocumentwithPayload() == 1

    }

    def "Count how many documents have term book"() {

        expect:
        demo.countNumberOfDocumentsHaveBookTerm() == 1

    }

    def "Suggestion adding and getting matching suggestions fuzzy" () {

        expect:
        demo.suggestionLoadandRetrieve().get(0).getString() == "hello"

    }

}
