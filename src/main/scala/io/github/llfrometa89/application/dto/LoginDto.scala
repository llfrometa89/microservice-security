package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class LoginDto(email: String, password: String)

object LoginDto {
  implicit val loginDecoder: Decoder[LoginDto]                            = deriveDecoder[LoginDto]
  implicit def loginEntityDecoder[F[_]: Sync]: EntityDecoder[F, LoginDto] = jsonOf
}
