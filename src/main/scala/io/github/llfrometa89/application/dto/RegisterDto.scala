package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class RegisterDto(email: String, password: String, firstName: String, lastName: String, cellPhone: Option[String])

object RegisterDto {
  implicit val registerDecoder: Decoder[RegisterDto]                            = deriveDecoder[RegisterDto]
  implicit def registerEntityDecoder[F[_]: Sync]: EntityDecoder[F, RegisterDto] = jsonOf
}
