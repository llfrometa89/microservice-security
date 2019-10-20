package io.github.llfrometa89.infrastructure.cross

import java.util.UUID

object IdGenerator {
  def generate: String = UUID.randomUUID().toString
}
