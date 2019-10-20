package io.github.llfrometa89.infrastructure.configurations

import cats.effect.{IO, LiftIO, Sync}
import cats.implicits._
import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._

trait ConfigFactory[F[_]] {
  def build: F[Configuration]
}

trait PureConfigFactoryInstances {

  def apply[F[_]](implicit F: ConfigFactory[F]): ConfigFactory[F] = F

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

case class Configuration(http: Http)

case class Http(server: HttpServer)

case class HttpServer(host: String, port: Int)
