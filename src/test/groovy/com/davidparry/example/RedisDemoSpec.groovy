package com.davidparry.example

import spock.lang.Shared
import spock.lang.Specification

class RedisDemoSpec extends Specification {

    @Shared
    RedisDemo demo = new RedisDemo()

    def setup() {
        demo.checkConnection()
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



}
