package io.github.llfrometa89.infrastructure.repositories

import cats.effect.Sync
import io.github.llfrometa89.domain.models.Profile
import io.github.llfrometa89.domain.repositories.ProfileRepository

trait UserProfileMysqlRepositoryInstances {
  implicit def instanceUserRepository[F[_]: Sync] = new ProfileRepository[F] {
    def register(userProfile: Profile): F[Profile] = {
      println("............Defining DB Storage")

      Sync[F].pure(userProfile)
    }
  }
}
