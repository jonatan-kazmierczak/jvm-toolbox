import scala.collection.JavaConverters._
import scala.util.parsing.json.JSON


/**
 * Functionalities implemented in Scala.
 *
 * @author Jonatan Kazmierczak (Jonatan (at) Son-of-God.info)
 */
object ScalaFunctionalities {
  def multiply( a: BigDecimal, b: BigDecimal ) = a * b

  /** This method is an interface of #multiply for Java. */
  def multiplyForJava( a: java.lang.Number, b: java.lang.Number ): java.math.BigDecimal =
    multiply( BigDecimal( a.toString ), BigDecimal( b.toString ) ).bigDecimal

  def countryNames( json: String ) =
    JSON.parseFull( json ).get.asInstanceOf[ List[Map[String,Any]] ].map( c => c("name").toString )

  /** This method is an interface of #countryNames for Java. */
  def countryNamesForJava( json: String ) = countryNames( json ).asJava

  def letter( data: Map[String, String] ) =
    s"""Dear ${data("hero_name")},
I like your ${data("hero_color")} color very much.

Kind regards,
${data("sender_name")}
"""

  /** This method is an interface of #letter for Java. */
  def letterForJava( data: java.util.Map[java.lang.String, java.lang.String] ) = letter( data.asScala.toMap )
}
