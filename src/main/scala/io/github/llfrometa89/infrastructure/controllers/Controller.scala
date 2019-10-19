package io.github.llfrometa89.infrastructure.controllers

import cats.effect.Sync
import org.http4s.HttpRoutes

trait Controller {
  def routes[F[_]: Sync]: HttpRoutes[F]
}
