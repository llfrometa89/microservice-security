package io.github.llfrometa89.infrastructure.gateways

import cats.effect.Sync
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User

trait UserCognitoGatewayInstances {

  implicit def instanceUserGateway[F[_]: Sync] = new UserGateway[F] {

    def register(user: User): F[User] = ???
  }
}
