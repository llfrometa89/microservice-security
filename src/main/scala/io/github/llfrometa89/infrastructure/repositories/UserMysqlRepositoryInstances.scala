package io.github.llfrometa89.infrastructure.repositories

import cats.effect.Sync
import io.github.llfrometa89.domain.models.User
import io.github.llfrometa89.domain.repositories.UserRepository

trait UserMysqlRepositoryInstances {

  implicit def instanceUserRepository[F[_]: Sync] = new UserRepository[F] {

    def register(user: User): F[User] = ???
  }
}
