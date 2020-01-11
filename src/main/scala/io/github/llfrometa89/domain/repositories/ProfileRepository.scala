package io.github.llfrometa89.domain.repositories

import io.github.llfrometa89.domain.models.Profile

trait ProfileRepository[F[_]] {

  def register(userProfile: Profile): F[Profile]
}

object ProfileRepository {

  def apply[F[_]](implicit F: ProfileRepository[F]): ProfileRepository[F] = F
}
