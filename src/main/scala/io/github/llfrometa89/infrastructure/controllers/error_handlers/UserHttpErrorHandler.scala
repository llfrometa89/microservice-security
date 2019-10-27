package io.github.llfrometa89.infrastructure.controllers.error_handlers

import cats.MonadError
import io.github.llfrometa89.domain.models.User.{UserAlreadyExists, UserError, UserNotFound, UserValidationError}
import org.http4s.Response
import io.github.llfrometa89.infrastructure.controllers.http.{
  BaseMessageResponse,
  ErrorMessage,
  LinkedMessage,
  MessageResponse
}

object UserHttpErrorHandler {
  def apply[F[_]: MonadError[?[_], UserError]]: HttpErrorHandler[F, UserError] =
    new RoutesHttpErrorHandler[F, UserError] {
      val A = implicitly

      val handler: UserError => F[Response[F]] = {
        case error @ UserAlreadyExists(username) =>
          BadRequest(
            BaseMessageResponse(
              MessageResponse(
                errorMessages = List(ErrorMessage(error.productPrefix, s"User already exists [username=$username]")))))
        case error @ UserNotFound(username) =>
          NotFound(
            BaseMessageResponse(
              MessageResponse(
                errorMessages = List(ErrorMessage(error.productPrefix, s"Not found user with [username=$username]")))))
        case UserValidationError(list) =>
          BadRequest(BaseMessageResponse(MessageResponse(linkedMessages = list.map(dv =>
            LinkedMessage(dv.errorCode, dv.errorMessage)))))
      }
    }
}
