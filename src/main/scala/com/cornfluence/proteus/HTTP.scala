package com.cornfluence.proteus

import com.cornfluence.proteus.models.ResultMessage
import com.typesafe.scalalogging.Logger

import scalaj.http.HttpResponse
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

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
            Left(ResultMessage(error = Option(true), Option(error.getMessage)))
        }
    }
  }

}