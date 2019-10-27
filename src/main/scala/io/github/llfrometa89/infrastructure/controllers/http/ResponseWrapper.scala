//package io.github.llfrometa89.infrastructure.controllers.http
//
//import cats.effect.Sync
//import io.circe.generic.semiauto.deriveDecoder
//import io.circe.{Decoder, Encoder, Json}
//import org.http4s.EntityDecoder
//import org.http4s.circe.jsonOf
//import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
//
//case class Result(key: String, value: String, field: BaseResponse[_])
//
//object Result {
//  implicit val registerDecoderwwww: Decoder[Result]                             = deriveDecoder[Result]
//  implicit def registerEntityDecoderwwwww[F[_]: Sync]: EntityDecoder[F, Result] = jsonOf
//}
//
//object ResponseWrapper {
//
//  def complete[D](data: D, notification: Option[NotificationMessages] = None)(
//      implicit encodeD: Encoder[D]): BaseResponse[D] =
//    BaseResponse(data, notification)
//
//  implicit val encodeResult: Encoder[Result] =
//    Encoder.forProduct3[String, String, BaseResponse[Json], Result]("key", "value", "fields") {
//      case Result(k, v, f) => (k, v, BaseResponse[Json](f.encoded))
//    }
//}
