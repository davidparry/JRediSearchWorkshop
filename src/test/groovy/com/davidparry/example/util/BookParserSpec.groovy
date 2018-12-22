package com.davidparry.example.util


import spock.lang.Specification

class BookParserSpec extends Specification {

    def "initial book is valid and has all lines to use in the examples"() {
        expect:
        new BookParser().parse("book").size() == 1479

    }


}
