package io.github.llfrometa89.domain.models

import io.github.llfrometa89.infrastructure.cross.UUIDGenerator

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
      UUIDGenerator.generate,
      user.userId.getOrElse(""),
      user.email,
      user.username,
      user.firstName,
      user.lastName,
      user.cellPhone)
}
