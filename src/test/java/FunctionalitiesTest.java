import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Java tests of the functionalities implemented in:
 * - Java ({@link JavaFunctionalities})
 * - Scala ({@link ScalaFunctionalities})
 * - Groovy ({@link GroovyFunctionalities})
 * - JavaScript (js_functionalities.js)
 *
 * @author Jonatan Kazmierczak (Jonatan (at) Son-of-God.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalitiesTest {

    /* Constants and pre-processed variables. */

    private static final BigDecimal NUM_DOUBLE_MUL_RESULT = new BigDecimal("1.00");

    private static final Integer NUM_INT = 1000000001;
    private static final BigDecimal NUM_INT_MUL_RESULT = BigDecimal.valueOf(1000000002000000001L);

    private static final Long NUM_LONG = 1000000000000000001L;
    private static final BigDecimal NUM_LONG_MUL_RESULT = new BigDecimal("1000000000000000002000000000000000001");

    private static final String JSON_SAMPLE =
            "[ {\"name\": \"Poland\", \"a\": 0}, {\"name\": \"Switzerland\", \"b\": [], \"c\": false} ]";
    private static final String JSON_SAMPLE_RESULT = "Switzerland";

    private static String jsonFull;
    private static final String JSON_FULL_RESULT = "Afghanistan";

//    private static final String LETTER_WORD = "Developer";

    private static ScriptEngine jse;
    private static SimpleBindings jseBindings;

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String[] testInfo = description.getMethodName().split("__");
            System.out.printf("%s,%s,%.2f%n", testInfo[0], testInfo[1], nanos / 1_000_000.0);
        }
    };


    /**
     * Initializes variables shared by several tests (like Nashorn instance)
     * to take off heavy initialization from tests themselves (to avoid biasing test execution times).
     */
    @BeforeClass
    public static void init() throws Exception {
        // Load JSON file (one liner)
        jsonFull = Files.newBufferedReader( Paths.get(
                FunctionalitiesTest.class.getResource("countries.json").toURI() ) ).readLine();
        //System.out.println("JSON length: " + jsonFull.length());

        jseBindings = new SimpleBindings() {{
            put("jsonFull", FunctionalitiesTest.jsonFull);
        }};

        // Init Nashorn with ES6
        System.setProperty("nashorn.args", "--language=es6");
        jse = new ScriptEngineManager().getEngineByExtension("js");
        //jse = new NashornScriptEngineFactory().getScriptEngine(new String[]{"--language=es6"});

        // Load (execute) JS code
        FunctionalitiesTest.jse.eval( Files.newBufferedReader( Paths.get(
                FunctionalitiesTest.class.getResource("js_functionalities.js").toURI() ) ), jseBindings);

        warmUp();
    }

    public static void warmUp() throws ScriptException {
        // Multiplication
        int a = Integer.MAX_VALUE;
        double b = 2.0;
        long c = Long.MAX_VALUE;
        JavaFunctionalities.multiply(a, b);
        JavaFunctionalities.multiply(c, c);
        ScalaFunctionalities.multiplyForJava(a, b);
        ScalaFunctionalities.multiplyForJava(c, c);
        GroovyFunctionalities.multiply(a, b);
        GroovyFunctionalities.multiply(c, c);
        jse.eval("multiply(" + a + ", " + b + ")", jseBindings);
        jse.eval("multiply(" + c + ", " + c + ")", jseBindings);

        // JSON parsing
        JavaFunctionalities.countryNames(JSON_SAMPLE);
        ScalaFunctionalities.countryNamesForJava(JSON_SAMPLE);
        GroovyFunctionalities.countryNames(JSON_SAMPLE);
        jse.eval("countryNamesForJava('" + JSON_SAMPLE + "')", jseBindings);

        // Template text processing
        Map<String, String> dataHolder = createDataHolder(
                "hero_name", "X", "hero_color", "Y", "sender_name", "Z");
        JavaFunctionalities.letter( dataHolder );
        ScalaFunctionalities.letterForJava( dataHolder );
        GroovyFunctionalities.letter( dataHolder );
    }


    // Multiplication

    @Test
    public void multiply_int_dbl_1__Java() {
        BigDecimal result = JavaFunctionalities.multiply(4, 0.25);
        assertEquals( NUM_DOUBLE_MUL_RESULT, result );
        //assertTrue( "1 expected", BigDecimal.ONE.compareTo( result ) == 0 );
    }

    @Test
    public void multiply_int_dbl_2__Scala() {
        BigDecimal result = ScalaFunctionalities.multiplyForJava( BigDecimal.valueOf(4), BigDecimal.valueOf(0.25) );
        assertEquals( NUM_DOUBLE_MUL_RESULT, result );
        //assertTrue( "1 expected", BigDecimal.ONE.compareTo( result ) == 0 );
    }

    @Test
    public void multiply_int_dbl_3__Groovy() {
        BigDecimal result = GroovyFunctionalities.multiply(4, 0.25);
        assertEquals( BigDecimal.ONE, result );
        //assertTrue( "1 expected", BigDecimal.ONE.compareTo( result ) == 0 );
    }

    @Test
    public void multiply_int_dbl_4__JS() throws ScriptException {
        Double result = (Double) jse.eval("multiply( 4, 0.25 )", jseBindings);
        assertEquals( Double.valueOf(1.0), result );
        //assertTrue( "1 expected", 1.0 == result );
    }

    @Test
    public void multiply_longs_1__Java() {
        BigDecimal result = JavaFunctionalities.multiply( NUM_LONG, NUM_LONG );
        assertEquals( NUM_LONG_MUL_RESULT, result );
    }

    @Test
    public void multiply_longs_2__Scala() {
        BigDecimal result = ScalaFunctionalities.multiplyForJava(
                BigDecimal.valueOf(NUM_LONG), BigDecimal.valueOf(NUM_LONG) );
        assertEquals( NUM_LONG_MUL_RESULT, result );
    }

    @Test
    public void multiply_longs_3__Groovy() {
        BigDecimal result = GroovyFunctionalities.multiply( NUM_LONG, NUM_LONG );
        assertEquals( NUM_LONG_MUL_RESULT, result );
    }

    @Test
    public void multiply_longs_4__JS() throws ScriptException {
        Double result = (Double) jse.eval("multiply( " + NUM_LONG + ", " + NUM_LONG + " )", jseBindings);
        //assertTrue( NUM_LONG_MUL_RESULT.compareTo( BigDecimal.valueOf(result) ) == 0 );
        assertEquals( NUM_LONG_MUL_RESULT, BigDecimal.valueOf(result) );
    }


    // Processing countries JSON
/*
    @Test
    public void countryNames_small__Scala() {
        List<String> result = ScalaFunctionalities.countryNamesForJava(JSON_SAMPLE);
        assertEquals( JSON_SAMPLE_RESULT, result.get(1) );
    }

    @Test
    public void countryNames_small__Groovy() {
        List<String> result = GroovyFunctionalities.countryNames(JSON_SAMPLE);
        assertEquals( JSON_SAMPLE_RESULT, result.get(1) );
    }

    @Test
    public void countryNames_small__JS() throws ScriptException {
        //String script = "countryNamesForJava( '" + JSON_SAMPLE + "' )";
        //System.out.println(script);
        String[] result = (String[]) jse.eval("countryNamesForJava( '" + JSON_SAMPLE + "' )", jseBindings);
        //System.out.println(Arrays.toString(result));
        assertEquals( JSON_SAMPLE_RESULT, result[1] );
    }
*/
    @Test
    public void countryNames_full_1__Java_JSONP() {
        List<String> result = JavaFunctionalities.countryNames(jsonFull);
        assertEquals( JSON_FULL_RESULT, result.get(0) );
    }

    @Test
    public void countryNames_full_2__Scala() {
        List<String> result = ScalaFunctionalities.countryNamesForJava(jsonFull);
        assertEquals( JSON_FULL_RESULT, result.get(0) );
    }

    @Test
    public void countryNames_full_3__Groovy() {
        List<String> result = GroovyFunctionalities.countryNames(jsonFull);
        assertEquals( JSON_FULL_RESULT, result.get(0) );
    }

    @Test
    public void countryNames_full_4__JS() throws ScriptException {
        String script = "countryNamesForJava( jsonFull )";
        //String script = "countryNamesForJava( '" + jsonFull + "' )";
        //System.out.println(script);
        String[] result = (String[]) jse.eval(script, jseBindings);
        //String[] result = (String[]) jse.eval(script);
        //System.out.println(Arrays.toString(result));
        assertEquals( JSON_FULL_RESULT, result[0] );
    }


    // Processing letter template

    @Test
    public void letter_1__Java() {
        String heroName = "Superman";
        String result = JavaFunctionalities.letter( createDataHolder(
                "hero_name", heroName, "hero_color", "blue", "sender_name", "Java Developer") );
        assertTrue( result.contains( heroName ) );
        //assertTrue( result.contains( LETTER_WORD ) );
    }

    @Test
    public void letter_2__Scala() {
        String heroName = "Harley Quinn";
        String result = ScalaFunctionalities.letterForJava( createDataHolder(
                "hero_name", heroName, "hero_color", "red", "sender_name", "Scala Developer") );
        assertTrue( result.contains( heroName ) );
    }

    @Test
    public void letter_3__Groovy() {
        String heroName = "Captain Cold";
        String result = GroovyFunctionalities.letter( createDataHolder(
                "hero_name", heroName, "hero_color", "blue", "sender_name", "Groovy Developer") );
        assertTrue( result.contains( heroName ) );
    }


    // Utilities

    /* To be replaced with Map.of from Java 9 - once Gradle works on JDK 9. */
    private static Map<String, String> createDataHolder(
            String k1, String v1, String k2, String v2, String k3, String v3) {
        return new HashMap<String, String>() {{
            put(k1, v1);
            put(k2, v2);
            put(k3, v3);
        }};
    }
}
