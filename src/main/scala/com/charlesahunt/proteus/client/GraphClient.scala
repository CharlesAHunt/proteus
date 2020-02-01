package com.charlesahunt.proteus.client

import cats.effect.Sync
import com.charlesahunt.proteus.config.ProteusConfig
import com.charlesahunt.proteus.models._
import com.charlesahunt.proteus.{DELETE, api, error, errorMessage, gharial, isError}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http._


/**
  * Manages Graph API operations
  *
  * @param graphName
  */
class GraphClient[F[_]](config: ProteusConfig, graphName: String)(implicit override val sync: Sync[F])
  extends ArangoClient[F](config: ProteusConfig)(sync: Sync[F]) {

  private val logger = Logger[GraphClient[F]]

  /**
    * Create the Graph
    *
    * @param edges
    * @return
    */
  def createGraph(
    edges: List[EdgeDefinition]): F[Either[Throwable, GraphResponse]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial")).postData(Graph(graphName, edges).asJson.noSpaces).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(s"Error creating graph with code {}}}")
        else ok.graph.toRight[Throwable](new Exception("Graph response missing"))
      case Left(error) =>
        logger.error("GraphClient.createGraph", error.getMessage)
        Left(error)
    }
  }

  /**
    * Drops Graph
    *
    * @return
    */
  def dropGraph: F[Either[Throwable, Boolean]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName").method(DELETE)).asString
    decode[DropGraphResponse](response.body) match {
      case Right(ok) =>
        if(ok.error) error(s"Error dropping graph with code ${ok.code.toString}")
        else Right(ok.removed)
      case Left(error) =>
        logger.error("GraphClient.dropGraph", error.getMessage)
        Left(error)
    }
  }

  /**
    * Adds a vertex to the given collection.
    *
    * @param collectionName
    * @return
    */
  def createVertexCollection(
    collectionName: String
  ): F[Either[Throwable, GraphResponse]] = sync.delay {
    val collection = CollectionName(collectionName)
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex").postData(collection.asJson.noSpaces)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
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
    * @param vertexCollection
    * @param json
    * @return
    */
  def createVertex(
    vertexCollection: String,
    json: String
  ): F[Either[Throwable, EdgeOrVertex]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex/$vertexCollection").postData(json)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
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
    collectionName: String,
    from: List[String],
    to: List[String]): F[Either[Throwable, List[EdgeDefinition]]] = sync.delay {
    val edge = EdgeDefinition(collectionName, from, to).asJson.noSpaces
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/").postData(edge)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
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
    collectionName: String,
    edgeType: String,
    from: String,
    to: String): F[Either[Throwable, EdgeOrVertex]] = sync.delay {
    val edge = Edge(edgeType, from, to).asJson.noSpaces
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/$collectionName").postData(edge)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else ok.edge.toRight[Throwable](new Exception("Edge response missing"))
      case Left(error) =>
        logger.error("GraphClient.createEdge", error.getMessage)
        Left(error)
    }
  }

  /**
    * Removes an edge from the collection.
    *
    * @param collectionName
    * @param edgeKey
    * @return
    */
  def deleteEdge(collectionName: String, edgeKey: String): F[Either[Throwable, Unit]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/$collectionName/$edgeKey").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("GraphClient.deleteEdge", error.getMessage)
        Left(error)
    }
  }

  /**
    * Removes a vertex from the collection.
    *
    * @param collectionName
    * @param vertexKey
    * @return
    */
  def deleteVertex(collectionName: String, vertexKey: String): F[Either[Throwable, Unit]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex/$collectionName/$vertexKey").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("GraphClient.deleteVertex", error.getMessage)
        Left(error)
    }
  }

  /**
    * Remove one edge definition from the graph. This will only remove the edge collection, the vertex collections
    *  remain untouched and can still be used in your queries.
    *
    * @param collectionName
    * @return
    */
  def deleteEdgeCollection(collectionName: String): F[Either[Throwable, Unit]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/edge/$collectionName").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("GraphClient.deleteEdgeCollection", error.getMessage)
        Left(error)
    }
  }

  /**
    * Removes a vertex collection from the graph and optionally deletes the collection, if it is not used in any other graph.
    *
    * @param collectionName
    * @return
    */
  def deleteVertexCollection(collectionName: String): F[Either[Throwable, Unit]] = sync.delay {
    val response = withAuth(Http(s"$arangoHost/$api/$gharial/$graphName/vertex/$collectionName").method(DELETE)).asString
    decode[ResultMessage](response.body) match {
      case Right(ok) =>
        if(isError(ok)) error(errorMessage(ok.errorMessage))
        else Right(())
      case Left(error) =>
        logger.error("GraphClient.deleteVertexCollection", error.getMessage)
        Left(error)
    }
  }

  //TODO modify edge, replace edge, and same for collection of edges
  //TODO modify vertex, replace vertex, and same for collection of vertices
}