package io.github.llfrometa89.infrastructure.controllers

import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import io.circe.{Decoder, Encoder}
import cats.Applicative
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

trait BaseCompleteResponse

case class BaseResponse[D](data: D) extends BaseCompleteResponse

object ResponseWrapper {

  def complete[D](data: D): BaseCompleteResponse = BaseResponse(data)

//  implicit val accountRequestEncoder: Encoder[BaseResponse[_]]                                   = deriveEncoder[BaseResponse[_]]
//  implicit def accountRequestEntityEncoder[F[_]: Applicative]: EntityEncoder[F, BaseResponse[_]] = jsonEncoderOf
}
