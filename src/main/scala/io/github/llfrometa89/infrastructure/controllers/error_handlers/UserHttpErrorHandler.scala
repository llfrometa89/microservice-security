package io.github.llfrometa89.infrastructure.controllers.error_handlers

import cats.MonadError
import io.github.llfrometa89.domain.models.User.{UserAlreadyExists, UserError, UserNotFound}
import org.http4s.Response
import org.http4s.circe._
import io.circe.syntax._

object UserHttpErrorHandler {
  def apply[F[_]: MonadError[?[_], UserError]]: HttpErrorHandler[F, UserError] =
    new RoutesHttpErrorHandler[F, UserError] {
      val A = implicitly

      val handler: UserError => F[Response[F]] = {
//        case InvalidUserAge(age)         => BadRequest(s"Invalid age $age".asJson)
        case UserAlreadyExists(username) => BadRequest(username.asJson)
        case UserNotFound(username)      => NotFound(username.asJson)
      }
    }
}
