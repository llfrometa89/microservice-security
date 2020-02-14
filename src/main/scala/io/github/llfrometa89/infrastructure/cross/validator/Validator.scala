package io.github.llfrometa89.infrastructure.cross.validator

import cats.data._

object Validator {
  trait ValidationMessage {
    def message: String
    def code: String
  }

  type ValidationResult[A] = ValidatedNec[ValidationMessage, A]
}
