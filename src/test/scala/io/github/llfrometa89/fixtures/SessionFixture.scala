package io.github.llfrometa89.fixtures

import io.github.llfrometa89.application.dto.SessionDto

trait SessionFixture {

  val username = "example@exa.mple"
  val password = "aPassword"

  val session = SessionDto(
    accessToken = "aAccessToken",
    refreshToken = "aRefreshToken",
    username = username,
    tokenType = "aTokenType",
    expiresIn = 111)
}
