package io.github.llfrometa89.infrastructure.configurations

import cats.effect.Sync
import io.github.llfrometa89.infrastructure.configurations.ConfigFactory.CanNotLoadResource
import pureconfig._
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

trait ConfigFactory[F[_]] {
  def build: F[Configuration]
}

object ConfigFactory {

  def apply[F[_]](implicit F: ConfigFactory[F]): ConfigFactory[F] = F

  case object ConfigError                                    extends Exception
  case class CanNotLoadResource(error: ConfigReaderFailures) extends Exception
}

trait PureConfigFactoryInstances {

  implicit def instanceConfigFactory[F[_]: Sync]: ConfigFactory[F] = new ConfigFactory[F] {

    def build: F[Configuration] =
      ConfigSource.default.load[Configuration] match {
        case Right(value) => Sync[F].pure(value)
        case Left(error)  => Sync[F].raiseError(CanNotLoadResource(error))
      }
  }
}

case class Configuration(http: HttpConfig, aws: AwsConfig)

case class HttpConfig(server: HttpServerConfig)
case class HttpServerConfig(host: String, port: Int)

case class AwsConfig(accessKey: String, secretKey: String, region: String, cognito: AwsCognitoConfig)
case class AwsCognitoConfig(userPoolId: String, appClientId: String)
