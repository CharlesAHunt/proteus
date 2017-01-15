package com.cornfluence.proteus

import com.typesafe.scalalogging.Logger

import scalaj.http.{HttpRequest, HttpResponse}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

import scala.concurrent.Future

trait HTTP {

  private val logger = Logger("HTTP")

  def handleResponse[T](response: HttpResponse[String]): Either[ResultMessage, HttpResponse[String]] = {
    response.code match {
      case c if c.toString.startsWith("2") => Right(response)
      case _ =>
        decode[ResultMessage](response.body) match {
          case Right(ok) => Left(ok)
          case Left(error) =>
            logger.error(error.getMessage)
            Left(ResultMessage(error = true, Option(error.getMessage)))
        }
    }
  }

}