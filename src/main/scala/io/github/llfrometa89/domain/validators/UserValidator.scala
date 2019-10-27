package io.github.llfrometa89.domain.validators

import io.github.llfrometa89.domain.models.User
import cats.implicits._
import cats.data._
import cats.data.Validated._
import io.github.llfrometa89.domain.models.User.DomainValidation

object UserValidator {

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  def validateUser(
      email: String,
      password: String,
      firstName: String,
      lastName: String,
      cellPhone: Option[String]): ValidationResult[User] =
    (validateEmail(email), validatePassword(password), validateFirstName(firstName), validateLastName(lastName))
      .mapN((validatedEmail, validatedPassword, validatedFirstName, validatedLastName) =>
        User(validatedEmail, validatedPassword, validatedFirstName, validatedLastName, cellPhone))

  private def validateEmail(userName: String): ValidationResult[String] =
    if (userName.nonEmpty) userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if (password.nonEmpty) password.validNec else PasswordDoesNotMeetCriteria.invalidNec

  private def validateFirstName(firstName: String): ValidationResult[String] =
    if (firstName.matches("^[a-zA-Z]+$")) firstName.validNec else FirstNameHasSpecialCharacters.invalidNec

  private def validateLastName(lastName: String): ValidationResult[String] =
    if (lastName.matches("^[a-zA-Z]+$")) lastName.validNec else LastNameHasSpecialCharacters.invalidNec

}

case object UsernameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "Username cannot contain special characters."
  def errorCode: String    = productPrefix

}

case object PasswordDoesNotMeetCriteria extends DomainValidation {
  def errorMessage: String =
    "Password must be at least 10 characters long, including an uppercase and a lowercase letter, one number and one special character."
  def errorCode: String = productPrefix
}

case object FirstNameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "First name cannot contain spaces, numbers or special characters."
  def errorCode: String    = productPrefix
}

case object LastNameHasSpecialCharacters extends DomainValidation {
  def errorMessage: String = "Last name cannot contain spaces, numbers or special characters."
  def errorCode: String    = productPrefix
}
