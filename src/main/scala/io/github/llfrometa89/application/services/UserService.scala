package io.github.llfrometa89.application.services

import cats.implicits._
import cats.effect.Sync
import io.github.llfrometa89.application.dto.{LoginDto, RegisterDto, SessionDto}
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User.UserValidationError
import io.github.llfrometa89.domain.models.{User, UserProfile}
import io.github.llfrometa89.domain.repositories.UserProfileRepository
import io.github.llfrometa89.domain.validators.UserValidator

trait UserService[F[_]] {

  def login(loginData: LoginDto): F[SessionDto]

  def register(registerDto: RegisterDto): F[String]
}

object UserService {

  def apply[F[_]](implicit ev: UserService[F]): UserService[F] = ev

}

trait UserServiceInstances {

  implicit def instanceUserService[F[_]: Sync: UserProfileRepository: UserGateway] = new UserService[F] {

    override def login(loginData: LoginDto): F[SessionDto] =
      UserGateway[F].login(loginData.email, loginData.password).map(SessionDto.fromSession)

    def register(registerDto: RegisterDto): F[String] =
      for {
        userParam   <- validateUser(registerDto: RegisterDto)
        user        <- UserGateway[F].register(userParam)
        userProfile <- UserProfileRepository[F].register(UserProfile.fromUser(user))
      } yield userProfile.profileId

    private def validateUser(registerDto: RegisterDto): F[User] = {
      UserValidator
        .validateUser(
          registerDto.email,
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
