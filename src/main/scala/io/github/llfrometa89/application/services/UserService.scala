package io.github.llfrometa89.application.services

import cats.implicits._
import cats.effect.Sync
import io.github.llfrometa89.application.dto.RegisterDto
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.{User, UserProfile}
import io.github.llfrometa89.domain.repositories.UserProfileRepository
import io.github.llfrometa89.domain.validators.{DomainValidation, UserValidator}

trait UserService[F[_]] {

  def register(registerDto: RegisterDto): F[String]
}

object UserService {

  def apply[F[_]](implicit F: UserService[F]): UserService[F] = F

  case class ValidationError(list: List[DomainValidation]) extends Exception

}

trait UserServiceInstances {

  import UserService._

  implicit def instanceUserService[F[_]: Sync: UserProfileRepository: UserGateway] = new UserService[F] {

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
        case Left(errors) =>
          println(s"-----------validateUser::errors=$errors")
          Sync[F].raiseError(ValidationError(errors.toList))
      }
    }
  }
}
