package io.github.llfrometa89.infrastructure.controllers

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.LoginDto
import io.github.llfrometa89.infrastructure.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object UserController extends Controller {

  def routes[F[_]: Sync]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case req @ POST -> Root / USERS =>
        for {
          loginDto <- req.as[LoginDto]
          _        <- Sync[F].delay(println(s".........>>>loginDto=$loginDto"))
          resp     <- Ok("")
        } yield resp
    }
  }
}
