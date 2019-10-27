package io.github.llfrometa89.domain.models

import io.github.llfrometa89.infrastructure.cross.IdGenerator

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

  //Messages
  sealed abstract class UserError(message: String = "")        extends Exception(message)
  case class UserAlreadyExists(email: String)                  extends UserError(email)
  case class UserNotFound(username: String)                    extends UserError(username)
  case class UserNotAuthorized(username: String)               extends UserError(username)
  case class UserValidationError(list: List[DomainValidation]) extends UserError

  //Validation Messages
  trait DomainValidation {
    def errorMessage: String
    def errorCode: String
  }

}

case class UserProfile(
    profileId: String,
    userId: String,
    email: String,
    username: String,
    firstName: String,
    lastName: String,
    cellPhone: Option[String])

object UserProfile {

  def fromUser(user: User): UserProfile =
    UserProfile(
      IdGenerator.generate,
      user.userId.getOrElse(""),
      user.email,
      user.username,
      user.firstName,
      user.lastName,
      user.cellPhone)
}
