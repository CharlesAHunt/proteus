package com.charlesahunt.proteus

import com.charlesahunt.proteus.config.Config
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http.{Http, HttpRequest, HttpResponse}

/**
  * Handles authentication using JWT headers
  */
trait Auth {

  private val logger = Logger("Auth")

  def postAuth(request: HttpRequest): HttpRequest = {
    val authData = Auth(Config.configuration.user, Config.configuration.password)
    val token = {
      val response: HttpResponse[String] = Http(s"${Config.configuration.host}/_open/auth").postData(authData.asJson.noSpaces).asString
      decode[Jwt](response.body) match {
        case Right(ok) => Right(ok.jwt)
        case Left(error) =>
          logger.error(error.getMessage)
          Left(error)
      }
    }
    token match {
      case Right(jwt) => authToken(request, jwt)
      case Left(error) =>
        logger.error(error.getMessage)
        request
    }
  }

  private def authToken(request: HttpRequest, jwt: String): HttpRequest =
    request.header("Authorization", s"bearer $jwt")

  case class Auth(username: String, password: String)
  case class Jwt(jwt: String, must_change_password: Boolean)
}