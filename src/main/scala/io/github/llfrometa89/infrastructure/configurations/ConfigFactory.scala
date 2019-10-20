package io.github.llfrometa89.infrastructure.configurations

import cats.effect.Sync
import cats.implicits._
import pureconfig._
import pureconfig.generic.auto._

trait ConfigFactory[F[_]] {
  def build: F[Configuration]
}

object ConfigFactory {

  def apply[F[_]](implicit F: ConfigFactory[F]): ConfigFactory[F] = F
}

trait PureConfigFactoryInstances {

  implicit def instanceConfigFactory[F[_]: Sync]: ConfigFactory[F] = new ConfigFactory[F] {

    def build: F[Configuration] =
      ConfigSource.default.load[Configuration] match {
        case Right(value) => Sync[F].pure(value)
        case Left(error) =>
          println(s"........>error=$error")
          Sync[F].raiseError(new Exception("error"))
      }
  }
}

case class Configuration(http: HttpConfig, aws: AwsConfig)

case class HttpConfig(server: HttpServerConfig)
case class HttpServerConfig(host: String, port: Int)

case class AwsConfig(accessKey: String, secretKey: String, region: String, cognito: AwsCognitoConfig)
case class AwsCognitoConfig(userPoolId: String)
