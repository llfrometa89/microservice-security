package io.github.llfrometa89.infrastructure.controllers

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.Login.LoginRequest
import io.github.llfrometa89.application.services.AuthService
import io.github.llfrometa89.domain.models.User.UserError
import io.github.llfrometa89.infrastructure.cross.http.HttpErrorHandler
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

case class AuthController[F[_]: Sync: AuthService]() extends Http4sDsl[F] {

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] {

    case req @ POST -> Root / AUTH / LOGIN =>
      for {
        loginData <- req.as[LoginRequest]
        session   <- AuthService[F].login(loginData)
        resp      <- Ok(session)
      } yield resp
  }

  def routes(implicit H: HttpErrorHandler[F, UserError]): HttpRoutes[F] = H.handle(httpRoutes)
}
