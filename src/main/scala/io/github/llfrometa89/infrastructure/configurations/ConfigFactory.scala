package io.github.llfrometa89.infrastructure.configurations

import cats.Applicative
import cats.effect.Sync
import cats.mtl.{ApplicativeAsk, DefaultApplicativeAsk}
import pureconfig.ConfigSource
import pureconfig.generic.auto._

object ConfigFactory {
  case class AppConfig(http: HttpConfig, aws: AwsConfig)
  case class HttpConfig(server: HttpServerConfig)
  case class HttpServerConfig(host: String, port: Int)
  case class AwsConfig(accessKey: String, secretKey: String, region: String, cognito: AwsCognitoConfig)
  case class AwsCognitoConfig(userPoolId: String, appClientId: String)

  def ask[F[_], A](implicit ev: ApplicativeAsk[F, A]): F[A] = ev.ask

  def loadConfig[F[_]: Sync]: F[AppConfig] =
    Sync[F].delay(ConfigSource.default.loadOrThrow[AppConfig])

  type HasAppConfig[F[_]] = ApplicativeAsk[F, AppConfig]
  type HasAwsConfig[F[_]] = ApplicativeAsk[F, AwsConfig]
}

trait ConfigFactoryInstances {
  import ConfigFactory._

  implicit def configReader[F[_]: Sync]: HasAppConfig[F] =
    new DefaultApplicativeAsk[F, AppConfig] {
      override val applicative: Applicative[F] = implicitly
      override def ask: F[AppConfig]           = loadConfig[F]
    }
}
