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

  /**
    * Create the Graph
    *
    * @param graphName
    * @param edges
    * @return
    */
  def createGraph(
    graphName: String,
    edges: List[EdgeDefinition]): Future[Either[Throwable, GraphResponse]] = Future {
    val response = auth(Http(s"$arangoHost/$api/gharial").postData(Graph(graphName, edges).asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.get) Left(new Exception(s"Error creating graph with code ${ok.code}"))
        else Right(ok.graph.get)
      case Left(error) =>
        logger.error("GraphClient.createGraph", error.getMessage)
        Left(error)
    }
  }

  /*
Drops Graph
*/
  def dropGraph(graphName: String): Future[Either[Throwable, Boolean]] = Future {
    val response = auth(Http(s"$arangoHost/$api/gharial/$graphName").method(DELETE)).asString
    decode[DropGraphResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(s"Error dropping graph with code ${ok.code}"))
        else Right(ok.removed)
      case Left(error) =>
        logger.error("GraphClient.dropGraph", error.getMessage)
        Left(error)
    }
  }

  /*
Creates a new vertex in the collection named collection
 */
  def createVertexCollection(
    graphName: String,
    collectionName: String
  ): Future[Either[Throwable, String]] = Future {
    val collection = CollectionName(collectionName)
    val response = auth(Http(s"$arangoHost/$api/gharial/$graphName/vertex").postData(collection.asJson.noSpaces)).asString
    println("rESP:  "+response)
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(ok.graph.get._id)
      case Left(error) =>
        logger.error("GraphClient.createVertex", error.getMessage)
        Left(error)
    }
  }

  /*
  Creates a new edge in the collection named collection
   */
  def createEdge(
    collectionName: String,
    from: String,
    to: String): Future[Either[Throwable, String]] = Future {
    val edge = Edge(from, to).asJson.noSpaces
    val response = auth(Http(s"$arangoHost/$api/edge/$collectionName").postData(edge)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else Right(ok._key.get)
      case Left(error) =>
        logger.error("GraphClient.createEdge", error.getMessage)
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
        logger.error("GraphClient.replaceEdge", error.getMessage)
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
        logger.error("GraphClient.getAllEdges", error.getMessage)
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
        logger.error("GraphClient.getEdge", error.getMessage)
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
        logger.error("GraphClient.deleteEdge", error.getMessage)
        Left(error)
    }
  }
}