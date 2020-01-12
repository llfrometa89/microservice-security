package io.github.llfrometa89.application.services

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.Login.LoginRequest
import io.github.llfrometa89.application.dto.Session
import io.github.llfrometa89.application.dto.Session.SessionResponse
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.repositories.ProfileRepository

trait AuthService[F[_]] {

  def login(loginData: LoginRequest): F[SessionResponse]
}

object AuthService {

  def apply[F[_]](implicit ev: AuthService[F]): AuthService[F] = ev

}

trait AuthServiceInstances {

  implicit def instanceAuthService[F[_]: Sync: ProfileRepository: UserGateway] = new AuthService[F] {

    def login(loginData: LoginRequest): F[SessionResponse] =
      UserGateway[F]
        .login(loginData.email, loginData.password)
        .map(Session.fromSession)
  }
}

object AuthServiceInstances extends AuthServiceInstances
