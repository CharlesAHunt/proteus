package com.cornfluence.proteus

import com.cornfluence.proteus.models._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

object ArangoClient {
  def apply(name: String) = new ArangoClient(databaseName = name)

  def apply(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) =
    new ArangoClient(host, port, https, databaseName)
}

trait Auth extends ArangoConfig {

  private val logger = Logger("Auth")

  def auth(request: HttpRequest)(implicit arangoHost: String): HttpRequest = {
    val auth = Auth(userName, password)
    lazy val token = {
      val response: HttpResponse[String] = Http(s"$arangoHost/_open/auth").postData(auth.asJson.noSpaces).asString
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
    request.header("Authorization",s"bearer $jwt")

  case class Auth(username: String, password: String)
  case class Jwt(jwt: String, must_change_password: Boolean)
}

/**
  * General Database management methods
  *
  * @param host
  * @param port
  * @param https
  * @param databaseName
  */
class ArangoClient(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) extends Auth {
  private val logger = Logger[ArangoClient]
  protected implicit val arangoHost: String = if (https) s"https://$host:$port" else s"http://$host:$port"
  private val database = "database"
  type JWT = String
  type DatabaseName = String

  def getCurrentDatabase: Future[Either[Throwable, CurrentDatabase]] = Future {
    val response: HttpResponse[String] = auth(Http(s"$arangoHost/$api/$database/current")).asString
    decode[CurrentDatabase](response.body) match {
      case Right(ok) => Right(ok)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }


  /*
   Retrieves the list of all existing databases
   */
  def getDatabaseList: Future[Either[Throwable, List[DatabaseName]]] = Future {
    val response: HttpResponse[String] = auth(Http(s"$arangoHost/$api/$database")).asString
    decode[ResultList](response.body) match {
      case Right(ok) => Right(ok.result)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Creates a new database
   */
  def createDatabase(dbName: String, users: Option[List[User]]): Future[Either[Throwable, Unit]] = Future {
    val postData = Database(dbName, users)
    val response: HttpResponse[String] = auth(Http(s"$arangoHost/$api/$database").postData(postData.asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(())
      case Left(error) => Left(error)
    }
  }

  /*
  Deletes the database along with all data stored in it
   */
  def deleteDatabase(dbName: String): Future[Either[Throwable, Unit]] = Future {
    val response: HttpResponse[String] = auth(Http(s"$arangoHost/$api/$database/$dbName").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(())
      case Left(error) => Left(error)
    }
  }

  /*
  Creates a new collection
  */
  def createCollection(collectionName: String): Future[Either[Throwable, Unit]] = Future {
    val postData = Collection(collectionName)
    val response: HttpResponse[String] = auth(Http(s"$arangoHost/$api/collection").postData(postData.asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(())
      case Left(error) => Left(error)
    }
  }
}