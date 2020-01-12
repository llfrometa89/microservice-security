package io.github.llfrometa89.infrastructure.cross.http

import cats.effect.Sync
import org.http4s.HttpRoutes

trait Controller {
  def routes[F[_]: Sync]: HttpRoutes[F]
}
