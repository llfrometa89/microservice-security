package io.github.llfrometa89.domain.models

import io.github.llfrometa89.infrastructure.cross.IdGenerator

case class User(
    id: String,
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    userExtId: Option[String])

case class UserExt(username: String, password: String, firstName: String, lastName: String)

object User {

  def apply(
      email: String,
      password: String,
      firstName: String,
      lastName: String,
      userExtId: Option[String] = None): User =
    new User(IdGenerator.generate, email, password, firstName, lastName, userExtId)
}
