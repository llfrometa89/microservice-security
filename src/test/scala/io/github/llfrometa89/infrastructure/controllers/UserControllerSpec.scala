package io.github.llfrometa89.infrastructure.controllers

import cats.effect.IO
import com.olegpy.meow.hierarchy._
import io.github.llfrometa89.BaseUnitTest
import io.github.llfrometa89.application.dto.Login.LoginRequest
import io.github.llfrometa89.application.dto.Session.SessionResponse
import io.github.llfrometa89.application.services.AuthService
import io.github.llfrometa89.fixtures.SessionFixture
import io.github.llfrometa89.infrastructure.controllers.error_handlers.UserHttpErrorHandler
import org.http4s.implicits._
import org.http4s.{Response, Status, _}

class UserControllerSpec extends BaseUnitTest with SessionFixture {

  implicit val authService = new AuthService[IO] {
    def login(loginData: LoginRequest): IO[SessionResponse] = IO(session)
  }

  val authControllerRoutes: HttpRoutes[IO] = AuthController[IO]().routes(UserHttpErrorHandler[IO])

  describe("login") {
    it("login success") {
      val loginRequest =
        Request[IO](Method.POST, uri"/auth/login").withEntity(LoginRequest(username, password))

      val loginResponse: Response[IO] = authControllerRoutes.orNotFound(loginRequest).unsafeRunSync()

      loginRequest.as[LoginRequest].unsafeRunSync.email shouldBe loginResponse
        .as[SessionResponse]
        .unsafeRunSync
        .username
      loginResponse.as[SessionResponse].unsafeRunSync shouldBe session
      loginResponse.status shouldBe Status.Ok
    }
  }

}
