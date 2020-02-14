package io.github.llfrometa89.infrastructure.gateways

import cats.effect.Sync
import cats.implicits._
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.cognitoidp.model._
import com.amazonaws.services.cognitoidp.{AWSCognitoIdentityProvider, AWSCognitoIdentityProviderClientBuilder}
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User.{UserAlreadyExists, UserNotAuthorized}
import io.github.llfrometa89.domain.models.{Session, User}
import io.github.llfrometa89.infrastructure.configurations.ConfigFactory._
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException
import scala.jdk.CollectionConverters._
import UserCognitoGatewayInstances._

trait UserCognitoGatewayInstances {

  implicit def instanceUserGateway[F[_]: Sync: HasAwsConfig] = new UserGateway[F] {

    def register(user: User): F[User] = {
      (for {
        config  <- ask[F, AwsConfig]
        client  <- clientBuilder(config)
        userReq <- userRequestBuilder(config, user)
        _       <- Sync[F].delay(client.adminCreateUser(userReq))
      } yield user).recoverWith {
        case _: UsernameExistsException => Sync[F].raiseError(UserAlreadyExists(user.email))
        case error                      => Sync[F].raiseError(error)
      }
    }

    def login(username: String, password: String): F[Session] = {
      val computation = for {
        config           <- ask[F, AwsConfig]
        client           <- clientBuilder(config)
        authReq          <- authRequestBuilder(username, password, config)
        authResp         <- Sync[F].delay(client.adminInitiateAuth(authReq))
        authChallengeReq <- authChallengeRequestBuilder(username, password, authResp, config)
        authResult <- Sync[F]
          .pure(authResp.getChallengeName == NewPasswordRequiredChallengeName)
          .ifM(
            Sync[F].delay(client.adminRespondToAuthChallenge(authChallengeReq).getAuthenticationResult),
            Sync[F].pure(authResp.getAuthenticationResult)
          )
      } yield Session(
        authResult.getAccessToken,
        authResult.getRefreshToken,
        username,
        authResult.getTokenType,
        authResult.getExpiresIn.toLong
      )

      computation.recoverWith {
        case _: NotAuthorizedException => Sync[F].raiseError(UserNotAuthorized(username))
        case error                     => Sync[F].raiseError(error)
      }
    }

    private def authRequestBuilder(username: String, password: String, config: AwsConfig): F[AdminInitiateAuthRequest] =
      Sync[F].delay(
        new AdminInitiateAuthRequest()
          .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
          .withClientId(config.cognito.appClientId)
          .withUserPoolId(config.cognito.userPoolId)
          .withAuthParameters(Map(UsernameField -> username, PasswordField -> password).asJava)
      )

    private def authChallengeRequestBuilder(
        username: String,
        password: String,
        authResp: AdminInitiateAuthResult,
        config: AwsConfig
    ): F[AdminRespondToAuthChallengeRequest] = {
      Sync[F].delay(
        new AdminRespondToAuthChallengeRequest()
          .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
          .withChallengeResponses(
            Map(UsernameField -> username, PasswordField -> password, NewPasswordField -> password).asJava
          )
          .withClientId(config.cognito.appClientId)
          .withUserPoolId(config.cognito.userPoolId)
          .withSession(authResp.getSession)
      )
    }

    private def clientBuilder(config: AwsConfig): F[AWSCognitoIdentityProvider] = {

      val credentials         = new BasicAWSCredentials(config.accessKey, config.secretKey)
      val credentialsProvider = new AWSStaticCredentialsProvider(credentials)

      Sync[F].delay(
        AWSCognitoIdentityProviderClientBuilder
          .standard()
          .withCredentials(credentialsProvider)
          .withRegion(config.region)
          .build()
      )
    }

    private def userRequestBuilder(config: AwsConfig, user: User): F[AdminCreateUserRequest] =
      Sync[F].delay(
        new AdminCreateUserRequest()
          .withUserPoolId(config.cognito.userPoolId)
          .withUsername(user.username)
          .withUserAttributes(
            new AttributeType()
              .withName(EmailField)
              .withValue(user.email),
            new AttributeType().withName(NameField).withValue(user.firstName),
            new AttributeType().withName(FamilyNameField).withValue(user.lastName),
            new AttributeType().withName(PhoneNumberField).withValue(user.cellPhone.getOrElse("")),
            new AttributeType().withName(EmailVerifiedField).withValue(DefaultEmailVerifiedValue)
          )
          .withTemporaryPassword(user.password)
          .withMessageAction(MessageActionValue)
          .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
          .withForceAliasCreation(false)
      )
  }
}

object UserCognitoGatewayInstances {

  final val EmailField                       = "email"
  final val NameField                        = "name"
  final val FamilyNameField                  = "family_name"
  final val PhoneNumberField                 = "phone_number"
  final val EmailVerifiedField               = "email_verified"
  final val DefaultEmailVerifiedValue        = "true"
  final val UsernameField                    = "USERNAME"
  final val PasswordField                    = "PASSWORD"
  final val NewPasswordField                 = "NEW_PASSWORD"
  final val MessageActionValue               = "SUPPRESS"
  final val NewPasswordRequiredChallengeName = "NEW_PASSWORD_REQUIRED"
}
