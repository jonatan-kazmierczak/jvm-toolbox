/*
 * Functionalities implemented in ECMAScript 5.1 - compatible with Nashorn in JDK 8.
 * Will be migrated to ES6 (JDK 9) when Gradle will work on JDK 9.
 *
 * @author Jonatan Kazmierczak (Jonatan (at) Son-of-God.info)
 */

function multiply( a, b ) { return a * b }

// temporarily for Java 8
function countryNames( json ) { return JSON.parse( json ).map( function (c) { return c.name } ) }
//function countryNames( json ) { return JSON.parse( json ).map( c => c.name ) }

function countryNamesForJava( json ) { return Java.to( countryNames(json), 'java.lang.String[]' ); }
