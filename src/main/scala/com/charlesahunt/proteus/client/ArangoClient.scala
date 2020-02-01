package com.charlesahunt.proteus.client

import cats.effect.Sync
import com.charlesahunt.proteus.config.ProteusConfig
import com.charlesahunt.proteus.models._
import com.charlesahunt.proteus.{Auth, DELETE, api, db, error, errorMessage, isError}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http._

class ArangoClient[F[_]](config: ProteusConfig)(implicit val sync: Sync[F]) extends Auth {

  val hostPort: String = s"${config.host}:${config.port}"

  private val logger = Logger[ArangoClient[F]]
  protected implicit val arangoHost: String = if(config.tls) s"https://$hostPort" else s"http://$hostPort"
  private val database = "database"
  type DatabaseName = String

  def getCurrentDatabase: F[Either[Throwable, CurrentDatabase]] = sync.delay {
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$api/$database/current")).asString
    decode[CurrentDatabase](response.body) match {
      case Right(ok) => Right(ok)
      case Left(error) =>
        logger.error("ArangoClient.getCurrentDatabase", error.getMessage)
        Left(error)
    }
  }


  /**
    * Retrieves the list of all existing databases
    *
    * @return
    */
  def getDatabaseList: F[Either[Throwable, List[DatabaseName]]] = sync.delay {
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$api/$database")).asString
    decode[ResultList](response.body) match {
      case Right(ok) => Right(ok.result)
      case Left(error) =>
        logger.error("ArangoClient.getDatabaseList", error.getMessage)
        Left(error)
    }
  }

  /**
    * Creates a new database
    *
    * @param dbName
    * @param users
    * @return
    */
  def createDatabase(dbName: String, users: Option[List[User]]): F[Either[Throwable, Unit]] = sync.delay {
    val postData = Database(dbName, users)
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$api/$database").postData(postData.asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("ArangoClient.createDatabase", error.getMessage)
        Left(error)
    }
  }

  /**
    * Deletes the database along with all data stored in it
    *
    * @param dbName
    * @return
    */
  def deleteDatabase(dbName: String): F[Either[Throwable, Unit]] = sync.delay {
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$api/$database/$dbName").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("ArangoClient.deleteDatabase", error.getMessage)
        Left(error)
    }
  }

  /**
    * Creates a new collection
    *
    * @param dbName
    * @param collectionName
    * @return
    */
  def createCollection(dbName: String, collectionName: String): F[Either[Throwable, CollectionResponse]] = sync.delay {
    val postData = Collection(collectionName)
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$db/$dbName/$api/collection").postData(postData.asJson.noSpaces)).asString
    decode[CollectionResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) error(errorMessage(ok.errorMessage))
        else Right(ok)
      case Left(error) =>
        logger.error("ArangoClient.createCollection", error.getMessage)
        Left(error)
    }
  }

  /**
    * Drops a collection
    *
    * @param dbName
    * @param collectionName
    * @return
    */
  def dropCollection(dbName: String, collectionName: String): F[Either[Throwable, CollectionResponse]] = sync.delay {
    val response: HttpResponse[String] = withAuth(Http(s"$arangoHost/$db/$dbName/$api/collection/$collectionName").method(DELETE)).asString
    decode[CollectionResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) error(errorMessage(ok.errorMessage))
        else Right(ok)
      case Left(error) =>
        logger.error("ArangoClient.dropCollection", error.getMessage)
        Left(error)
    }
  }
}