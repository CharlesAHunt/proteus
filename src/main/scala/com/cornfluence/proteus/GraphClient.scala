package com.cornfluence.proteus

import com.cornfluence.proteus.models._
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

object GraphClient {
  def apply(name: String) = new GraphClient(databaseName = name)

  def apply(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) =
    new GraphClient(hostMachine, port, https, databaseName)
}

class GraphClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String)
  extends ArangoClient(hostMachine, port, https, databaseName) with Auth {

  private val logger = Logger[DocumentClient]

  /*
Creates a new Graph
*/
  def createGraph(
    db: String,
    name: String,
    edges: List[EdgeDefinition]): Future[Either[Throwable, GraphRes]] = Future {
    val response = auth(Http(s"$arangoHost/$api/gharial").postData(Graph(name, edges).asJson.noSpaces)).asString
    println("R: " + response.body)
    decode[GraphResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(s"Error creating graph with code ${ok.code}"))
        else Right(ok.graph)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
Drops a new Graph
*/
  def dropGraph(
    db: String,
    name: String): Future[Either[Throwable, Boolean]] = Future {
    val response = auth(Http(s"$arangoHost/$api/gharial/$name").method(DELETE)).asString
    decode[DropGraphResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(s"Error dropping graph with code ${ok.code}"))
        else Right(ok.removed)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
Creates a new vertex in the collection named collection
 */
  def createVertex(
    db: String,
    collectionName: String,
    data: String): Future[Either[Throwable, String]] = Future {
    val response = auth(Http(s"$arangoHost/$api/vertex/$collectionName").postData(data)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

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
    to: String): Future[Either[Throwable, String]] = Future {
    val edge = Edge(from, to)
    val response = auth(Http(s"$arangoHost/$api/edge/$collectionName").postData(edgeString)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Replaces a edge with edgeString
   */
  def replaceEdge(db: String, collectionName: String, edgeId: String, documentString: String)
  : Future[Either[Throwable, String]] = Future {
    val response = auth(Http(s"$arangoHost/$api/edge/collectionName/$edgeId").put(documentString)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Returns a list of all URI for all edges from the collection identified by collectionName.
   */
  def getAllEdges(db: String, collectionName: String, vertexStart: String, direction: Option[String] = None)
  : Future[Either[Throwable, List[Edge]]] = Future {
    val request = auth(Http(s"$arangoHost/$api/edges/$collectionName").param("vertex", vertexStart))
    val response = if (direction.nonEmpty) request.param("direction", direction.get).asString else request.asString
    decode[Edges](response.body) match {
      case Right(ok) =>
        if (ok.error) Left(new Exception("Error getting all edges"))
        else Right(ok.edges)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Retrieve an edge using its unique URI:
   */
  def getEdge(db: String, collectionName: String, edgeId: String): Future[Either[Throwable, Edge]] = Future {
    val response = auth(Http(s"$arangoHost/$api/edge/$collectionName/$edgeId")).asString
    decode[Edge](response.body) match {
      case Right(ok) =>
        //if(ok.error) Left(new Exception("Error getting all edges"))
        Right(ok)
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }

  /*
  Deletes an edge using its unique URI:
   */
  def deleteEdge(db: String, collectionName: String, edgeId: String): Future[Either[Throwable, Unit]] = Future {
    val response = auth(Http(s"$arangoHost/$api/edge/$collectionName/$edgeId").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(())
      case Left(error) =>
        logger.error(error.getMessage)
        Left(error)
    }
  }
}