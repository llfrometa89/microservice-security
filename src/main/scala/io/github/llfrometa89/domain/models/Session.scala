package io.github.llfrometa89.domain.models

case class Session(accessToken: String, refreshToken: String, username: String, tokenType: String, expiresIn: Long)
