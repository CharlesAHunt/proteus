package com.cornfluence.proteus

import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

import scala.concurrent.ExecutionContext.Implicits.global

object ArangoClient {
  def apply(name: String) = new ArangoClient(databaseName = name)

  def apply(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) =
    new ArangoClient(host, port, https, databaseName)
}

/**
  * General Database management and Auth API methods
  *
  * @param host
  * @param port
  * @param https
  * @param databaseName
  */
class ArangoClient(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {
  private val logger = Logger[ArangoClient]
  protected val arangoHost: String = if (https) s"https://$host:$port" else s"http://$host:$port"
  private val api = "_api"
  private val database = "database"
  type JWT = String
  type DatabaseName = String

  def auth(username: String, password: String): Future[Either[Throwable, JWT]] = Future {
    val auth = HTTP.Auth(username, password)
    val response: HttpResponse[String] = Http(s"$arangoHost/_open/auth").postData(auth.asJson.noSpaces).asString
    //{"error":true,"errorMessage":"Wrong credentials","code":401,"errorNum":401}
    decode[HTTP.JWT](response.body) match {
      case Right(ok) => Right(ok.jwt)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  def getCurrentDatabase: Future[Either[Throwable, CurrentDatabase]] = Future {
    val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database/current").asString
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
    val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database").asString
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
    val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database").postData(postData.asJson.noSpaces).asString
    println("BODY:  "+response.code)
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(ok.errorMessage.get))
        else Right(())
      case Left(error) => Left(error)
    }
  }

  /*
  Deletes the database along with all data stored in it
   */
  def deleteDatabase(dbName: String): Future[Either[Throwable, Unit]] = Future {
    val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database/$dbName").method("DELETE").asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(ok.errorMessage.get))
        else Right(())
      case Left(error) => Left(error)
    }
  }
}