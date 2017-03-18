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
    val response = auth(Http(s"$arangoHost/$api/$gharial").postData(Graph(graphName, edges).asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(s"Error creating graph with code ${ok.code}"))
        else ok.graph.toRight[Throwable](new Exception("Graph response missing"))
      case Left(error) =>
        logger.error("GraphClient.createGraph", error.getMessage)
        Left(error)
    }
  }

  /**
    * Drops Graph
    *
    * @param graphName
    * @return
    */
  def dropGraph(graphName: String): Future[Either[Throwable, Boolean]] = Future {
    val response = auth(Http(s"$arangoHost/$api/$gharial/$graphName").method(DELETE)).asString
    decode[DropGraphResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) Left(new Exception(s"Error dropping graph with code ${ok.code}"))
        else Right(ok.removed)
      case Left(error) =>
        logger.error("GraphClient.dropGraph", error.getMessage)
        Left(error)
    }
  }

  /**
    * Adds a vertex to the given collection.
    *
    * @param graphName
    * @param collectionName
    * @return
    */
  def createVertexCollection(
    graphName: String,
    collectionName: String
  ): Future[Either[Throwable, GraphResponse]] = Future {
    val collection = CollectionName(collectionName)
    val response = auth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex").postData(collection.asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else ok.graph.toRight[Throwable](new Exception("Graph reesponse missing"))
      case Left(error) =>
        logger.error("GraphClient.createVertexCollection", error.getMessage)
        Left(error)
    }
  }

  /**
    * Adds a vertex to the given collection.
    * free style json body
    *
    * @param graphName
    * @param vertexCollection
    * @param json
    * @return
    */
  def createVertex(
    graphName: String,
    vertexCollection: String,
    json: String
  ): Future[Either[Throwable, EdgeOrVertex]] = Future {
    val response = auth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex/$vertexCollection").postData(json)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else ok.vertex.toRight[Throwable](new Exception("Vertex missing from response"))
      case Left(error) =>
        logger.error("GraphClient.createVertex", error.getMessage)
        Left(error)
    }
  }

  /**
    * Adds an additional edge definition to the graph.
    *
    * @param collectionName
    * @param from
    * @param to
    * @return
    */
  def createEdgeCollection(
    graphName: String,
    collectionName: String,
    from: List[String],
    to: List[String]): Future[Either[Throwable, List[EdgeDefinition]]] = Future {
    val edge = EdgeDefinition(collectionName, from, to).asJson.noSpaces
    val response = auth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/").postData(edge)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else ok.graph.map(_.edgeDefinitions).toRight[Throwable](new Exception("Edge definition response missing"))
      case Left(error) =>
        logger.error("GraphClient.createEdgeCollection", error.getMessage)
        Left(error)
    }
  }

  /**
    * Creates a new edge in the collection. Within the body the has to contain a _from and _to value referencing
    * to valid vertices in the graph. Furthermore the edge has to be valid in the definition of this edge collection.
    *
    *  free-style json body
    *
    * @param collectionName
    * @param from
    * @param to
    * @return
    */
  def createEdge(
    graphName: String,
    collectionName: String,
    edgeType: String,
    from: String,
    to: String): Future[Either[Throwable, EdgeOrVertex]] = Future {
    val edge = Edge(edgeType, from, to).asJson.noSpaces
    val response = auth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/$collectionName").postData(edge)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
        else ok.edge.toRight[Throwable](new Exception("Edge response missing"))
      case Left(error) =>
        logger.error("GraphClient.createEdge", error.getMessage)
        Left(error)
    }
  }

  //TODO modify edge, replace edge, delete edge, and same for collection of edges
  //TODO modify vertex, replace vertex, delete vertex, and same for collection of vertices


}