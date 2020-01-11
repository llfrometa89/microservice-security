package io.github.llfrometa89.domain.models

import io.github.llfrometa89.infrastructure.cross.validator.Validator.ValidationMessage

case class User(
    email: String,
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    cellPhone: Option[String],
    userId: Option[String] = None)

object User {

  def apply(email: String, password: String, firstName: String, lastName: String, cellPhone: Option[String]): User =
    new User(email, email.split("\\@").head, password, firstName, lastName, cellPhone)

  sealed abstract class UserError                               extends Exception
  case class UserAlreadyExists(email: String)                   extends UserError
  case class UserNotFound(username: String)                     extends UserError
  case class UserNotAuthorized(username: String)                extends UserError
  case class UserValidationError(list: List[ValidationMessage]) extends UserError
}
