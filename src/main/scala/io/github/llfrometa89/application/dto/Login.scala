package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Login {
  case class LoginRequest(email: String, password: String)

  object LoginRequest {
    implicit val loginEncoder: Encoder[LoginRequest]                            = deriveEncoder[LoginRequest]
    implicit def loginEntityEncoder[F[_]: Sync]: EntityEncoder[F, LoginRequest] = jsonEncoderOf
    implicit val loginDecoder: Decoder[LoginRequest]                            = deriveDecoder[LoginRequest]
    implicit def loginEntityDecoder[F[_]: Sync]: EntityDecoder[F, LoginRequest] = jsonOf
  }
}
