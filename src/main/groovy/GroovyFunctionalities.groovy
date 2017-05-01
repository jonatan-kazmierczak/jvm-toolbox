import groovy.json.JsonParserType
import groovy.json.JsonSlurper


/**
 * Functionalities implemented in Groovy.
 *
 * @author Jonatan Kazmierczak (Jonatan (at) Son-of-God.info)
 */
class GroovyFunctionalities {
    static BigDecimal multiply( Number a, Number b ) { BigDecimal.ONE * a * b }

    static List<String> countryNames( String json ) {
        new JsonSlurper( type: JsonParserType.INDEX_OVERLAY ).parseText( json ).collect { c -> c.name }
    }

    static String letter( Map<String, String> data ) {
        """\
Dear $data.hero_name,
I like your $data.hero_color color very much.

Kind regards,
$data.sender_name
"""
    }
}
