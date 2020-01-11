package io.github.llfrometa89.application.services

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.Login.LoginRequest
import io.github.llfrometa89.application.dto.Register.{RegisterRequest, RegisterResponse}
import io.github.llfrometa89.application.dto.Session
import io.github.llfrometa89.application.dto.Session.SessionResponse
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User.UserValidationError
import io.github.llfrometa89.domain.models.{User, UserProfile}
import io.github.llfrometa89.domain.repositories.UserProfileRepository
import io.github.llfrometa89.domain.validators.UserValidator

trait UserService[F[_]] {

  def login(loginData: LoginRequest): F[SessionResponse]

  def register(registerDto: RegisterRequest): F[RegisterResponse]
}

object UserService {

  def apply[F[_]](implicit ev: UserService[F]): UserService[F] = ev

}

trait UserServiceInstances {

  implicit def instanceUserService[F[_]: Sync: UserProfileRepository: UserGateway] = new UserService[F] {

    override def login(loginData: LoginRequest): F[SessionResponse] =
      UserGateway[F].login(loginData.username, loginData.password).map(Session.fromSession)

    def register(registerDto: RegisterRequest): F[RegisterResponse] =
      for {
        userParam   <- validateUser(registerDto: RegisterRequest)
        user        <- UserGateway[F].register(userParam)
        userProfile <- UserProfileRepository[F].register(UserProfile.fromUser(user))
      } yield RegisterResponse(userProfile.profileId)

    private def validateUser(registerDto: RegisterRequest): F[User] = {
      UserValidator
        .validateUser(
          registerDto.username,
          registerDto.password,
          registerDto.firstName,
          registerDto.lastName,
          registerDto.cellPhone)
        .toEither match {
        case Right(value) => Sync[F].pure(value)
        case Left(errors) => Sync[F].raiseError(UserValidationError(errors.toList))
      }
    }
  }
}

object UserServiceInstances extends UserServiceInstances
