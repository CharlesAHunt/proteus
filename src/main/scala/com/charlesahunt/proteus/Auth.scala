package com.charlesahunt.proteus

import com.charlesahunt.proteus.config.Config
import com.charlesahunt.proteus.models.Error
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http.{Http, HttpRequest, HttpResponse}

/**
  * Handles authentication with JWT headers
  */
trait Auth {

  private val logger = Logger("Auth")
  type JWT = String
  protected lazy val JWT = postAuth match {
    case Right(ok) => ok
    case Left(error) =>
      logger.error(error.message)
      ""
  }

  /**
    * Retrieves a JWT for the configured user from the ArangoDB server
    *
    * @return HttpRequest with JWT in the Authorization header as a bearer token
    */
  def postAuth: Either[Error, JWT] = {
    val authData = Auth(Config.configuration.user, Config.configuration.password)
    val response: HttpResponse[String] = Http(s"${Config.configuration.host.toString}/_open/auth")
      .postData(authData.asJson.noSpaces).asString
    decode[Jwt](response.body) match {
      case Right(ok) => Right(ok.jwt)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(Error(error.getMessage))
    }
  }

  def withAuth(request: HttpRequest): HttpRequest =
    request.header("Authorization", s"bearer ${JWT.toString}")

  case class Auth(username: String, password: String)

  case class Jwt(jwt: String, must_change_password: Boolean)

}