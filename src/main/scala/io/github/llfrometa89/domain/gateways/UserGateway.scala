package io.github.llfrometa89.domain.gateways

import io.github.llfrometa89.domain.models.User

trait UserGateway[F[_]] {

  def register(user: User): F[User]
}

object UserGateway {

  def apply[F[_]](implicit F: UserGateway[F]): UserGateway[F] = F
}
