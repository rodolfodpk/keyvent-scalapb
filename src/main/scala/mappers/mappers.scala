package mappers

;

import com.trueaccord.scalapb.TypeMapper
// import java.time.Instant

abstract class InstantMapper[BaseType, CustomType]() extends TypeMapper[BaseType, CustomType]

object TypeMappers {

  def get[E, A](xor: Either[E, A]): A = xor match {
    case Right(res) ⇒ res
    case Left(e)    ⇒ throw new Exception(s"Parse error: ${e}")
  }

//  private def applyInstant(millis: Long): Instant = Instant.ofEpochMilli(millis)
//  private def unapplyInstant(dt: Instant): Long = dt.toEpochMilli
//  implicit val instantMapper: TypeMapper[Long, Instant] = TypeMapper(applyInstant)(unapplyInstant)

  /*
    private def applyInstantOpt(millis: Int64Value): Instant = Instant.ofEpochMilli(millis.value)
    private def unapplyInstantOpt(dt: Instant): Int64Value = Int64Value(dt.toEpochMilli)
    implicit val instantOptMapper: TypeMapper[Int64Value, Instant] = TypeMapper(applyInstantOpt)(unapplyInstantOpt)
  */

}