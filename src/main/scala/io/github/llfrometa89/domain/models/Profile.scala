package io.github.llfrometa89.domain.models

import io.github.llfrometa89.infrastructure.cross.UUIDGenerator

case class Profile(
    id: String,
    userId: String,
    email: String,
    username: String,
    firstName: String,
    lastName: String,
    cellPhone: Option[String]
)

object Profile {

  def fromUser(user: User): Profile =
    Profile(
      id = UUIDGenerator.generate,
      userId = user.userId.getOrElse(""),
      email = user.email,
      username = user.username,
      firstName = user.firstName,
      lastName = user.lastName,
      cellPhone = user.cellPhone
    )
}
