package io.github.llfrometa89.infrastructure.controllers

import cats.effect.IO
import io.github.llfrometa89.application.services.UserService
import io.github.llfrometa89.infrastructure.controllers.error_handlers.UserHttpErrorHandler
import org.http4s.implicits._
import org.http4s.{Response, Status, _}
import com.olegpy.meow.hierarchy._
import io.github.llfrometa89.BaseUnitTest
import io.github.llfrometa89.application.dto.Login.LoginRequest
import io.github.llfrometa89.application.dto.Register.{RegisterRequest, RegisterResponse}
import io.github.llfrometa89.application.dto.Session.SessionResponse
import io.github.llfrometa89.fixtures.SessionFixture

class UserControllerSpec extends BaseUnitTest with SessionFixture {

  implicit val userService: UserService[IO] = new UserService[IO] {
    def login(loginData: LoginRequest): IO[SessionResponse]          = IO(session)
    def register(registerDto: RegisterRequest): IO[RegisterResponse] = ???
  }

  val userControllerRoutes: HttpRoutes[IO] = UserController[IO]().routes(UserHttpErrorHandler[IO])

  describe("login") {
    it("login success") {
      val loginRequest =
        Request[IO](Method.POST, uri"/users/login").withEntity(LoginRequest(username, password))

      val loginResponse: Response[IO] = userControllerRoutes.orNotFound(loginRequest).unsafeRunSync()

      loginRequest.as[LoginRequest].unsafeRunSync.username shouldBe loginResponse
        .as[SessionResponse]
        .unsafeRunSync
        .username
      loginResponse.as[SessionResponse].unsafeRunSync shouldBe session
      loginResponse.status shouldBe Status.Ok
    }
  }

}
