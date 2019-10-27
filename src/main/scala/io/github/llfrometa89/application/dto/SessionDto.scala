package io.github.llfrometa89.application.dto

import cats.effect.Sync
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import io.github.llfrometa89.domain.models.Session
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class SessionDto(accessToken: String, refreshToken: String, username: String, tokenType: String, expiresIn: Long)

object SessionDto {

  def fromSession(session: Session) =
    SessionDto(session.accessToken, session.refreshToken, session.username, session.tokenType, session.expiresIn)

  implicit val sessionEncoder: Encoder[SessionDto]                            = deriveEncoder[SessionDto]
  implicit def sessionEntityEncoder[F[_]: Sync]: EntityEncoder[F, SessionDto] = jsonEncoderOf
}
