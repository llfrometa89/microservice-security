package io.github.llfrometa89.infrastructure.controllers.error_handlers

import cats.MonadError
import io.github.llfrometa89.domain.models.User._
import org.http4s.Response
import io.github.llfrometa89.infrastructure.cross.http.ResponseWrapper._

object UserHttpErrorHandler {
  def apply[F[_]: MonadError[?[_], UserError]]: HttpErrorHandler[F, UserError] =
    new RoutesHttpErrorHandler[F, UserError] {
      val A = implicitly

      val handler: UserError => F[Response[F]] = {
        case error @ UserAlreadyExists(username) =>
          BadRequest(
            SuccessResponse(
              MessagesResponse(MessageResponse(error.productPrefix, s"User already exists [username=$username]"))))
        case error @ UserNotFound(username) =>
          NotFound(
            SuccessResponse(
              MessagesResponse(MessageResponse(error.productPrefix, s"Not found user with [username=$username]"))))
        case UserValidationError(list) =>
          BadRequest(SuccessResponse(MessagesResponse(list.map(dv => MessageResponse(dv.code, dv.message)))))
        case error @ UserNotAuthorized(username) =>
          BadRequest( //TODO Unauthorized
            SuccessResponse(
              MessagesResponse(MessageResponse(error.productPrefix, s"Incorrect username[$username] or password "))))
      }
    }
}
