package io.github.llfrometa89.infrastructure.controllers

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.RegisterDto
import io.github.llfrometa89.application.services.UserService
import io.github.llfrometa89.infrastructure.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object UserController {

  def routes[F[_]: Sync: UserService]: HttpRoutes[F] = {

    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case req @ POST -> Root / USERS / REGISTER =>
        for {
          registerData <- req.as[RegisterDto]
          _            <- Sync[F].delay(println(s".........>>>registerData=$registerData"))
          result       <- UserService[F].register(registerData)
          _            <- Sync[F].delay(println(s".........>>>registerData222=$result"))
          resp         <- Ok(result)
        } yield resp
    }
  }
}
