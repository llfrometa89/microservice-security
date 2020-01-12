package io.github.llfrometa89

import cats.effect.{ConcurrentEffect, ExitCode, IO, IOApp, Timer}
import com.olegpy.meow.hierarchy._
import fs2.Stream
import io.github.llfrometa89.infrastructure.controllers.{AuthController, UserController}
import io.github.llfrometa89.infrastructure.controllers.error_handlers.UserHttpErrorHandler
import io.github.llfrometa89.infrastructure.implicits._
import org.http4s.HttpApp
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import cats.implicits._
import org.http4s.implicits._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = WebServer.run[IO].compile.drain.as(ExitCode.Success)

}

object WebServer {

  def run[F[_]: ConcurrentEffect](implicit T: Timer[F]): Stream[F, Nothing] = {

    val userRoute = UserController[F]().routes(UserHttpErrorHandler[F])
    val authRoute = AuthController[F]().routes(UserHttpErrorHandler[F])
    val routes    = userRoute <+> authRoute

    val httpApp: HttpApp[F]         = routes.orNotFound
    val httpAppWithLoggerMiddleware = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

    BlazeServerBuilder[F]
      .bindHttp(9000, "0.0.0.0")
      .withHttpApp(httpAppWithLoggerMiddleware)
      .serve
  }.drain
}
