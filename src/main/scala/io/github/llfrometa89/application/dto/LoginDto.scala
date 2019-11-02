package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class LoginDto(username: String, password: String)

object LoginDto {
  implicit val loginEncoder: Encoder[LoginDto]                            = deriveEncoder[LoginDto]
  implicit val loginDecoder: Decoder[LoginDto]                            = deriveDecoder[LoginDto]
  implicit def loginEntityEncoder[F[_]: Sync]: EntityEncoder[F, LoginDto] = jsonEncoderOf
  implicit def loginEntityDecoder[F[_]: Sync]: EntityDecoder[F, LoginDto] = jsonOf
}
