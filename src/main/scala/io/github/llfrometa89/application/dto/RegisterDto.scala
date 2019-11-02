package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class RegisterDto(
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    cellPhone: Option[String])

object RegisterDto {
  implicit val registerEncoder: Encoder[RegisterDto]                            = deriveEncoder[RegisterDto]
  implicit val registerDecoder: Decoder[RegisterDto]                            = deriveDecoder[RegisterDto]
  implicit def registerEntityEncoder[F[_]: Sync]: EntityEncoder[F, RegisterDto] = jsonEncoderOf
  implicit def registerEntityDecoder[F[_]: Sync]: EntityDecoder[F, RegisterDto] = jsonOf
}
