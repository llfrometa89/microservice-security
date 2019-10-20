package io.github.llfrometa89

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import cats.effect.{ConcurrentEffect, ContextShift, Timer}
import fs2.Stream
import io.github.llfrometa89.infrastructure.controllers.UserController
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

import io.github.llfrometa89.infrastructure.implicits._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = WebServer.run[IO].compile.drain.as(ExitCode.Success)

}

object WebServer {
  def run[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    val httpApp                     = UserController.routes[F].orNotFound
    val httpAppWithLoggerMiddleware = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
    BlazeServerBuilder[F]
      .bindHttp(9000, "0.0.0.0")
      .withHttpApp(httpAppWithLoggerMiddleware)
      .serve
  }.drain
}
