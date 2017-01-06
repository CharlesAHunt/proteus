package com.cornfluence.proteus

import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

object GraphClient {
  def apply(name: String) = new GraphClient(databaseName = name)

  def apply(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) =
    new GraphClient(hostMachine, port, https, databaseName)
}

class GraphClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String)
  extends ArangoClient(hostMachine, port, https, databaseName) {

  private val logger = Logger[DocumentClient]

  /*
  Creates a new edge in the collection named collection
   */
  def createEdge(
    db: String,
    collectionName: String,
    edgeString: String,
    fromCollection: String,
    toCollection: String,
    from: String,
    to: String,
    createIfNotExists: Boolean = true): Future[Either[Error, String]] = Future {

    val response = Http(s"$arangoHost/db/$db/_api/edge").postData(edgeString)
      .param("collection", collectionName)
      .param("createCollection", createIfNotExists.toString)
      .param("from", s"$fromCollection/$from")
      .param("to", s"$toCollection/$to").postData(edgeString).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(ok.errorMessage.get))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }


  /*
  Replaces a edge with edgeString
   */
  def replaceEdge(db: String, collectionName: String, edgeId: String, documentString: String): Future[Either[Error, String]] = Future {
    val response = Http(s"$arangoHost/db/$db/_api/edge/collectionName/$edgeId").put(documentString).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(ok.errorMessage.get))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Returns a list of all URI for all edges from the collection identified by collectionName.
   */
  def getAllEdges(db: String, collectionName: String, vertexStart: String, direction: Option[String] = None): Future[String] = Future {
    val request = Http(s"$arangoHost/db/$db/_api/edges/$collectionName").param("vertex", vertexStart)
    if (direction.nonEmpty) request.param("direction", direction.get).asString else request.asString
  }

  /*
  Retrieve an edge using its unique URI:
   */
  def getEdge(db: String, collectionName: String, edgeId: String): Future[String] = Future {
    Http(s"$arangoHost/db/$db/_api/edge/$collectionName/$edgeId").asString
  }

  /*
  Deletes an edge using its unique URI:
   */
  def deleteEdge(db: String, collectionName: String, edgeId: String): Future[Either[Error, String]] = Future {
    val response = Http(s"$arangoHost/db/$db/_api/edge/$collectionName/$edgeId").method("DELETE").asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(ok.errorMessage.get))
        else Right("success")
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }
}