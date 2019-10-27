package io.github.llfrometa89.infrastructure.controllers.http

import cats.Applicative
import cats.effect.Sync
import cats.syntax.functor._
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

trait Message {
  val code: String
  val value: String
}

case class ErrorMessage(code: String, value: String)   extends Message
case class WarningMessage(code: String, value: String) extends Message
case class LinkedMessage(code: String, value: String)  extends Message

case class MessageResponse(
    errorMessages: List[ErrorMessage] = List.empty[ErrorMessage],
    warningMessages: List[WarningMessage] = List.empty[WarningMessage],
    linkedMessages: List[LinkedMessage] = List.empty[LinkedMessage])

object MessageResponse {
  implicit val mrE: Encoder[MessageResponse]                       = deriveEncoder[MessageResponse]
  implicit def meEE[F[_]: Sync]: EntityEncoder[F, MessageResponse] = jsonEncoderOf
}

case class BaseMessageResponse(messages: MessageResponse)

object BaseMessageResponse {
  implicit val brE: Encoder[BaseMessageResponse]                              = deriveEncoder[BaseMessageResponse]
  implicit def brEE[F[_]: Applicative]: EntityEncoder[F, BaseMessageResponse] = jsonEncoderOf
}

object GenericMessageDerivation {

  implicit val encodeEvent: Encoder[Message] = Encoder.instance {
    case error: ErrorMessage     => error.asJson
    case warning: WarningMessage => warning.asJson
  }

  implicit val decodeEvent: Decoder[Message] =
    List[Decoder[Message]](
      Decoder[ErrorMessage].widen,
      Decoder[WarningMessage].widen
    ).reduceLeft(_ or _)
}
