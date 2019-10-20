package io.github.llfrometa89.application.services

import cats.implicits._
import cats.effect.Sync
import io.github.llfrometa89.application.dto.RegisterDto
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User
import io.github.llfrometa89.domain.repositories.UserRepository
import io.github.llfrometa89.domain.validators.UserValidator

trait UserService[F[_]] {

  def register(registerDto: RegisterDto): F[String]
}

trait UserServiceInstances {

  def apply[F[_]](implicit F: UserService[F]): UserService[F] = F

  def instance[F[_]: Sync: UserRepository: UserGateway] = new UserService[F] {

    def register(registerDto: RegisterDto): F[String] =
      for {
        user        <- validateUser(registerDto: RegisterDto)
        userCreated <- UserGateway[F].register(user)
        userSaved   <- UserRepository[F].register(userCreated)
      } yield userSaved.id

    private def validateUser(registerDto: RegisterDto): F[User] = {
      UserValidator
        .validateUser(registerDto.email, registerDto.password, registerDto.firstName, registerDto.lastName)
        .toEither match {
        case Right(value) => Sync[F].pure(value)
        case Left(errors) => Sync[F].raiseError(new Exception("eeeee"))
      }
    }
  }

}
