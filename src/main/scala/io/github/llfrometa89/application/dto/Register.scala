package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Register {
  case class RegisterRequest(
      username: String,
      password: String,
      firstName: String,
      lastName: String,
      cellPhone: Option[String]
  )

  case class RegisterResponse(profileId: String)

  object RegisterRequest {
    implicit val registerDecoder: Decoder[RegisterRequest]                            = deriveDecoder[RegisterRequest]
    implicit def registerEntityDecoder[F[_]: Sync]: EntityDecoder[F, RegisterRequest] = jsonOf
  }

  object RegisterResponse {
    implicit val registerEncoder: Encoder[RegisterResponse]                            = deriveEncoder[RegisterResponse]
    implicit def registerEntityEncoder[F[_]: Sync]: EntityEncoder[F, RegisterResponse] = jsonEncoderOf
  }
}
