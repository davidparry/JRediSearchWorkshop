import com.davidparry.example.RedisBase
import spock.lang.Specification

/**
 * Test that a connection can take place.
 * Since everything depends on a Index the only way to test a connection is to call dropIndex when one is to current.
 */
class RedisBaseSpec extends Specification {

    def "Can we connect, dropIndex only way"() {
        expect:
        !new RedisBase().getIndexClient().dropIndex(true)
    }
}
