package com.charlesahunt.proteus

import com.charlesahunt.proteus.client.ArangoClient
import com.charlesahunt.proteus.models.Error
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
      logger.error(error.message)
      ""
  }

  /**
    * Retrieves a JWT for the configured user from the ArangoDB server
    *
    * @return HttpRequest with JWT in the Authorization header as a bearer token
    */
  def postAuth: Either[Error, JWT] = {
    val authData = Auth(config.user, config.password)
    val response: HttpResponse[String] = Http(s"$arangoHost/_open/auth")
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