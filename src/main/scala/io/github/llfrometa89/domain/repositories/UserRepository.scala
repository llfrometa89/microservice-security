package io.github.llfrometa89.domain.repositories

import io.github.llfrometa89.domain.models.User

trait UserRepository[F[_]] {

  def register(user: User): F[User]
}

object UserRepository {

  def apply[F[_]](implicit F: UserRepository[F]): UserRepository[F] = F
}
