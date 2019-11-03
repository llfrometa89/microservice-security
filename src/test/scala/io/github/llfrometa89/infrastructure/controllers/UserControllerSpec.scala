package io.github.llfrometa89.infrastructure.controllers

import cats.effect.IO
import io.github.llfrometa89.application.dto.{LoginDto, RegisterDto, SessionDto}
import io.github.llfrometa89.application.services.UserService
import io.github.llfrometa89.infrastructure.controllers.error_handlers.UserHttpErrorHandler
import org.http4s.implicits._
import org.http4s.{Response, Status, _}
import com.olegpy.meow.hierarchy._
import io.github.llfrometa89.BaseUnitTest
import io.github.llfrometa89.fixtures.SessionFixture

class UserControllerSpec extends BaseUnitTest with SessionFixture {

  implicit val userService: UserService[IO] = new UserService[IO] {
    override def login(loginData: LoginDto): IO[SessionDto]     = IO(session)
    override def register(registerDto: RegisterDto): IO[String] = ???
  }

  val userControllerRoutes: HttpRoutes[IO] = UserController[IO]().routes(UserHttpErrorHandler[IO])

  describe("login") {
    it("login success") {
      val loginRequest =
        Request[IO](Method.POST, uri"/users/login").withEntity(LoginDto(username, password))

      val loginResponse: Response[IO] = userControllerRoutes.orNotFound(loginRequest).unsafeRunSync()

      loginRequest.as[LoginDto].unsafeRunSync.username shouldBe loginResponse.as[SessionDto].unsafeRunSync.username
      loginResponse.as[SessionDto].unsafeRunSync shouldBe session
      loginResponse.status shouldBe Status.Ok
    }
  }

}
