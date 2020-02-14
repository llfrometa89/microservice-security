package io.github.llfrometa89.infrastructure.implicits.syntax

import cats.data.Validated.{Invalid, Valid}
import cats.effect.Sync
import cats.implicits._
import io.github.llfrometa89.infrastructure.cross.validator.Validator.{ValidationMessage, ValidationResult}

trait ValidatedSyntax {
  implicit class ImplicitValidatedSyntax[T](validated: ValidationResult[T]) {
    def liftF[F[_]: Sync](f: List[ValidationMessage] => Exception): F[T] = validated match {
      case Invalid(e) => Sync[F].raiseError(f(e.toList))
      case Valid(a)   => Sync[F].pure(a)
    }
  }
}

object ValidatedSyntax extends ValidatedSyntax
