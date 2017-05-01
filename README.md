# JVM Toolbox 2017 - Demo
This is a demo of a polyglot JVM application built with Gradle (see https://github.com/gradle/gradle/issues/1549).

It demonstrates the following functionalities:
* calculation
* JSON processing
* text template processing

implemented in the following JVM languages:
* Java ~~9~~ 8 (required as long as Gradle crashes on JDK 9)
* Scala 2.12.1+
* Groovy 2.4+
* JavaScript 
  - ~~ECMAScript 2015 (ES6)~~ ECMAScript 5.1 (required as long as Gradle crashes on JDK 9)
  - executed by Nashorn engine (included in JDK)

All the functionalities are invoked from unit tests written in Java -
they are showing how functionalities written in various JVM languages
can be invoked from Java code.  
There are also benchmarks collected from unit tests execution.

## Test cases execution
### Prerequisites
Successful tests execution requires the following tools:
* JDK ~~9~~ 8 (required as long as Gradle crashes on JDK 9)
* newest Gradle

### Execution
To execute unit tests please run the following command:
```bash
gradle test
```

You may notice, that there are 2 failing tests.
That's correct behavior - they are exposing limitation of tested functionality.
