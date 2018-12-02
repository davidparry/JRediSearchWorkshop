package com.davidparry.example


import spock.lang.Specification

class RedisDemoSpec extends Specification {

    def "do we have a client "() {
        expect:
        new RedisDemo().getClient() != null
    }

    def "well what about a valid connection"() {
        when:
        RedisDemo demo = new RedisDemo()

        then:
        demo.checkConnection()

    }


}
