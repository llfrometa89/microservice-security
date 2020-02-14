package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.github.llfrometa89.domain.models.{Session => SessionModel}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Session {

  def fromSession(session: SessionModel): SessionResponse =
    SessionResponse(session.accessToken, session.refreshToken, session.username, session.tokenType, session.expiresIn)

  case class SessionResponse(
      accessToken: String,
      refreshToken: String,
      username: String,
      tokenType: String,
      expiresIn: Long
  )

  object SessionResponse {

    implicit val sessionEncoder: Encoder[SessionResponse]                            = deriveEncoder[SessionResponse]
    implicit val sessionDecoder: Decoder[SessionResponse]                            = deriveDecoder[SessionResponse]
    implicit def sessionEntityEncoder[F[_]: Sync]: EntityEncoder[F, SessionResponse] = jsonEncoderOf
    implicit def sessionEntityDecoder[F[_]: Sync]: EntityDecoder[F, SessionResponse] = jsonOf
  }
}
