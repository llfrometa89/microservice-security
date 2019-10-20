package io.github.llfrometa89.domain.repositories

import io.github.llfrometa89.domain.models.UserProfile

trait UserProfileRepository[F[_]] {

  def register(userProfile: UserProfile): F[UserProfile]
}

object UserProfileRepository {

  def apply[F[_]](implicit F: UserProfileRepository[F]): UserProfileRepository[F] = F
}
