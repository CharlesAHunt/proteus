package com.charlesahunt.proteus

import com.charlesahunt.proteus.client.ArangoClient
import com.charlesahunt.proteus.models.{ArangoError, ProteusError}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http.{Http, HttpRequest, HttpResponse}

/**
  * Handles authentication with JWT headers
  */
trait Auth[F[_]] { client: ArangoClient[F] =>

  private val logger = Logger("Auth")
  type JWT = String
  val hostPort: String = s"${client.config.host}:${client.config.port}"
  protected implicit lazy val arangoHost: String = if(config.tls) s"https://$hostPort" else s"http://$hostPort"
  protected lazy val JWT = postAuth match {
    case Right(ok) => ok
    case Left(error) =>
      logger.error(error.errorMessage)
      ""
  }

  /**
    * Retrieves a JWT for the configured user from the ArangoDB server
    *
    * @return HttpRequest with JWT in the Authorization header as a bearer token
    */
  def postAuth: Either[ProteusError, JWT] = {
    val authData = Auth(config.user, config.password)
    val response: HttpResponse[String] = Http(s"$arangoHost/_open/auth")
      .postData(authData.asJson.noSpaces).asString
    if(response.is2xx) decodeSuccessResponse(response) else decodeErrorResponse(response)
  }

  def decodeErrorResponse(response: HttpResponse[String]): Either[ProteusError, JWT] = {
    decode[ArangoError](response.body) match {
      case Right(ok) => Left(ProteusError(ok.errorMessage))
      case Left(error) =>
        logger.error(error.getMessage)
        Left(ProteusError(error.getMessage))
    }
  }

  def decodeSuccessResponse(response: HttpResponse[String]): Either[ProteusError, JWT] = {
    decode[Jwt](response.body) match {
      case Right(ok) => Right(ok.jwt)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(ProteusError(error.getMessage))
    }
  }

  def withAuth(request: HttpRequest): HttpRequest =
    request.header("Authorization", s"bearer ${JWT.toString}")

  case class Auth(username: String, password: String)

  case class Jwt(jwt: String)

}