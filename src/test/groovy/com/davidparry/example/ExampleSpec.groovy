package com.davidparry.example

import spock.lang.Specification

/**
 * We continue...
 */
class ExampleSpec extends Specification {

    def "Now lets create our Schema"() {
        expect:
        new RedisBookExample().createSchema()

    }
}
