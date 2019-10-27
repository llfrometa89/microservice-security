package io.github.llfrometa89.infrastructure.gateways

import cats.effect.Sync
import cats.implicits._
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.cognitoidp.model._
import com.amazonaws.services.cognitoidp.{AWSCognitoIdentityProvider, AWSCognitoIdentityProviderClientBuilder}
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User.{UserAlreadyExists, UserNotAuthorized}
import io.github.llfrometa89.domain.models.{Session, User}
import io.github.llfrometa89.infrastructure.configurations.{AwsConfig, ConfigFactory}
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException

import scala.collection.JavaConverters._

trait UserCognitoGatewayInstances {

  implicit def instanceUserGateway[F[_]: Sync: ConfigFactory] = new UserGateway[F] {

    final val EMAIL_FIELD                          = "email"
    final val NAME_FIELD                           = "name"
    final val FAMILY_NAME_FIELD                    = "family_name"
    final val PHONE_NUMBER_FIELD                   = "phone_number"
    final val EMAIL_VERIFIED_FIELD                 = "email_verified"
    final val EMPTY                                = "email_verified"
    final val DEFAULT_EMAIL_VERIFIED_VALUE         = "true"
    final val MESSAGE_ACTION_VALUE                 = "SUPPRESS"
    final val USERNAME_FIELD                       = "USERNAME"
    final val PASSWORD_FIELD                       = "PASSWORD"
    final val NEW_PASSWORD_FIELD                   = "NEW_PASSWORD"
    final val NEW_PASSWORD_REQUIRED_CHALLENGE_NAME = "NEW_PASSWORD_REQUIRED"

    def register(user: User): F[User] = {
      (for {
        config  <- ConfigFactory[F].build
        client  <- clientBuilder(config.aws)
        userReq <- userRequestBuilder(config.aws, user)
        _       <- Sync[F].delay(client.adminCreateUser(userReq))
      } yield user).recoverWith {
        case _: UsernameExistsException => Sync[F].raiseError(UserAlreadyExists(user.email))
        case error                      => Sync[F].raiseError(error)
      }
    }

    def login(username: String, password: String): F[Session] = {
      val computation = for {
        config           <- ConfigFactory[F].build
        client           <- clientBuilder(config.aws)
        authReq          <- authRequestBuilder(username, password, config.aws)
        authResp         <- Sync[F].delay(client.adminInitiateAuth(authReq))
        authChallengeReq <- authChallengeRequestBuilder(username, password, authResp, config.aws)
        authResult <- Sync[F]
          .pure(authResp.getChallengeName == NEW_PASSWORD_REQUIRED_CHALLENGE_NAME)
          .ifM(
            Sync[F].delay(client.adminRespondToAuthChallenge(authChallengeReq).getAuthenticationResult),
            Sync[F].pure(authResp.getAuthenticationResult))
      } yield
        Session(
          authResult.getAccessToken,
          authResult.getRefreshToken,
          username,
          authResult.getTokenType,
          authResult.getExpiresIn.toLong)

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
          .withAuthParameters(Map(USERNAME_FIELD -> username, PASSWORD_FIELD -> password).asJava))

    private def authChallengeRequestBuilder(
        username: String,
        password: String,
        authResp: AdminInitiateAuthResult,
        config: AwsConfig): F[AdminRespondToAuthChallengeRequest] = {
      Sync[F].delay(
        new AdminRespondToAuthChallengeRequest()
          .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
          .withChallengeResponses(
            Map(USERNAME_FIELD -> username, PASSWORD_FIELD -> password, NEW_PASSWORD_FIELD -> password).asJava)
          .withClientId(config.cognito.appClientId)
          .withUserPoolId(config.cognito.userPoolId)
          .withSession(authResp.getSession))
    }

    private def clientBuilder(config: AwsConfig): F[AWSCognitoIdentityProvider] = {

      val credentials         = new BasicAWSCredentials(config.accessKey, config.secretKey)
      val credentialsProvider = new AWSStaticCredentialsProvider(credentials)

      Sync[F].delay(
        AWSCognitoIdentityProviderClientBuilder
          .standard()
          .withCredentials(credentialsProvider)
          .withRegion(config.region)
          .build())
    }

    private def userRequestBuilder(config: AwsConfig, user: User): F[AdminCreateUserRequest] =
      Sync[F].delay(
        new AdminCreateUserRequest()
          .withUserPoolId(config.cognito.userPoolId)
          .withUsername(user.username)
          .withUserAttributes(
            new AttributeType()
              .withName(EMAIL_FIELD)
              .withValue(user.email),
            new AttributeType().withName(NAME_FIELD).withValue(user.firstName),
            new AttributeType().withName(FAMILY_NAME_FIELD).withValue(user.lastName),
            new AttributeType().withName(PHONE_NUMBER_FIELD).withValue(user.cellPhone.getOrElse(EMPTY)),
            new AttributeType().withName(EMAIL_VERIFIED_FIELD).withValue(DEFAULT_EMAIL_VERIFIED_VALUE)
          )
          .withTemporaryPassword(user.password)
          .withMessageAction(MESSAGE_ACTION_VALUE)
          .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
          .withForceAliasCreation(false))
  }
}
