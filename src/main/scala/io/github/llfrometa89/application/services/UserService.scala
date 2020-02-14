package io.github.llfrometa89.application.services

import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.application.dto.Register.{RegisterRequest, RegisterResponse}
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User.UserValidationError
import io.github.llfrometa89.domain.models.{Profile, User}
import io.github.llfrometa89.domain.repositories.ProfileRepository
import io.github.llfrometa89.domain.validators.UserValidator
import io.github.llfrometa89.infrastructure.cross.validator.Validator.ValidationMessage
import io.github.llfrometa89.infrastructure.implicits.validated._

trait UserService[F[_]] {
  def register(registerDto: RegisterRequest): F[RegisterResponse]
}

object UserService {
  def apply[F[_]](implicit ev: UserService[F]): UserService[F] = ev
}

trait UserServiceInstances {
  implicit def instanceUserService[F[_]: Sync: ProfileRepository: UserGateway] = new UserService[F] {
    def register(registerDto: RegisterRequest): F[RegisterResponse] =
      for {
        validUser <- validateUser(registerDto: RegisterRequest)
        user      <- UserGateway[F].register(validUser)
        profile   <- ProfileRepository[F].register(Profile.fromUser(user))
      } yield RegisterResponse(profile.id)

    private def validateUser(data: RegisterRequest): F[User] = {
      UserValidator
        .validateUser(data.username, data.password, data.firstName, data.lastName, data.cellPhone)
        .liftF(validationErrorFn)
    }

    private def validationErrorFn(errors: List[ValidationMessage]): Exception = UserValidationError(errors)
  }
}

object UserServiceInstances extends UserServiceInstances
