package io.github.llfrometa89.infrastructure.cross

import java.util.UUID

object UUIDGenerator {
  def generate: String = UUID.randomUUID().toString
}
