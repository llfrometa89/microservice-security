package io.github.llfrometa89.infrastructure.gateways

import cats.effect.Sync
import cats.implicits._
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.services.cognitoidp.model.{
  AdminCreateUserRequest,
  AttributeType,
  DeliveryMediumType,
  UsernameExistsException
}
import com.amazonaws.services.cognitoidp.{AWSCognitoIdentityProvider, AWSCognitoIdentityProviderClientBuilder}
import io.github.llfrometa89.domain.gateways.UserGateway
import io.github.llfrometa89.domain.models.User
import io.github.llfrometa89.domain.models.User.UserAlreadyExists
import io.github.llfrometa89.infrastructure.configurations.{AwsConfig, ConfigFactory}

trait UserCognitoGatewayInstances {

  implicit def instanceUserGateway[F[_]: Sync: ConfigFactory] = new UserGateway[F] {

    final val EMAIL_FIELD                  = "email"
    final val NAME_FIELD                   = "name"
    final val FAMILY_NAME_FIELD            = "family_name"
    final val PHONE_NUMBER_FIELD           = "phone_number"
    final val EMAIL_VERIFIED_FIELD         = "email_verified"
    final val EMPTY                        = "email_verified"
    final val DEFAULT_EMAIL_VERIFIED_VALUE = "true"
    final val MESSAGE_ACTION_VALUE         = "SUPPRESS"

    def register(user: User): F[User] = {
      (for {
        config  <- ConfigFactory[F].build
        client  <- clientBuilder(config.aws)
        userReq <- Sync[F].delay(userRequestBuilder(config.aws, user))
        _       <- Sync[F].delay(client.adminCreateUser(userReq))
      } yield user).recoverWith {
        case _: UsernameExistsException => Sync[F].raiseError(UserAlreadyExists(user.email))
        case error                      => Sync[F].raiseError(error)
      }
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

    private def userRequestBuilder(config: AwsConfig, user: User): AdminCreateUserRequest = {

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
        .withForceAliasCreation(false)
    }
  }
}
