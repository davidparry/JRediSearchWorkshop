//
// Built on Sat Dec 15 21:08:59 CET 2018 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
//
// Built on Sat Dec 15 21:11:30 CET 2018 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR

appender("STDOUT", ConsoleAppender) {
    layout(PatternLayout) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n"
    }
}
logger("org.springframework.web", ERROR, ["STDOUT"], false)
logger("com.davidparry", DEBUG, ["STDOUT"], false)
root(ERROR, ["STDOUT"])