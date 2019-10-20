package io.github.llfrometa89.infrastructure.repositories

import cats.effect.Sync
import io.github.llfrometa89.domain.models.UserProfile
import io.github.llfrometa89.domain.repositories.UserProfileRepository

trait UserProfileMysqlRepositoryInstances {

  implicit def instanceUserRepository[F[_]: Sync] = new UserProfileRepository[F] {

    def register(userProfile: UserProfile): F[UserProfile] = {

      println("............Defining DB Storage")

      Sync[F].pure(userProfile)
    }
  }
}
