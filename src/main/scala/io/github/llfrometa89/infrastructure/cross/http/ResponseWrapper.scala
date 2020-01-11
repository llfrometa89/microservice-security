package io.github.llfrometa89.infrastructure.cross.http

import cats.Applicative
import cats.effect.Sync
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object ResponseWrapper {

  case class MessageResponse(code: String, message: String)

  object MessageResponse {

    implicit val messageResponseEncoder: Encoder[MessageResponse]                            = deriveEncoder[MessageResponse]
    implicit def messageResponseEntityEncoder[F[_]: Sync]: EntityEncoder[F, MessageResponse] = jsonEncoderOf
  }

  case class MessagesResponse(
      errors: List[MessageResponse],
      warnings: List[MessageResponse],
      linked: List[MessageResponse])

  case class SuccessResponse(messages: MessagesResponse)

  object MessagesResponse {

    def apply(error: MessageResponse): MessagesResponse =
      new MessagesResponse(List(error), warnings = List.empty[MessageResponse], linked = List.empty[MessageResponse])

    def apply(errors: List[MessageResponse]): MessagesResponse =
      new MessagesResponse(errors, warnings = List.empty[MessageResponse], linked = List.empty[MessageResponse])

    implicit val messagesResponseEncoder: Encoder[MessagesResponse]                            = deriveEncoder[MessagesResponse]
    implicit def messagesResponseEntityEncoder[F[_]: Sync]: EntityEncoder[F, MessagesResponse] = jsonEncoderOf
  }

  object SuccessResponse {
    implicit val successResponseEncoder: Encoder[SuccessResponse]                                   = deriveEncoder[SuccessResponse]
    implicit def SuccessResponseEntityEncoder[F[_]: Applicative]: EntityEncoder[F, SuccessResponse] = jsonEncoderOf
  }

}
