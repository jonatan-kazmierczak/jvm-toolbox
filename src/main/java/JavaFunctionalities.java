import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;


/**
 * Functionalities implemented in Java.
 *
 * @author Jonatan Kazmierczak (Jonatan (at) Son-of-God.info)
 */
public interface JavaFunctionalities {
    static BigDecimal multiply( Number a, Number b ) {
        return new BigDecimal( a.toString() ).multiply( new BigDecimal( b.toString() ) );
    }

    static String letter( Map<String, String> data ) {
        return String.format( "Dear %s,%nI like your %s color very much.%n%nKind regards,%n%s",
                data.get("hero_name"), data.get("hero_color"), data.get("sender_name") );
    }

    /*
     * This method has dependency on external JSON-P libraries (2 jars).
     */
    static List<String> countryNames( String json ) {
        return Json.createReader( new StringReader( json ) ).readArray()
                .stream().map( c -> ((JsonObject) c).getString("name") ).collect( Collectors.toList() );
    }
}
