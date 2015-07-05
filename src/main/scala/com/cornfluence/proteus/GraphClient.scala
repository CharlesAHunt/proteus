package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._


class GraphClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) extends ArangoClient(hostMachine, port, https, databaseName) {

   /*
   Creates a new edge in the collection named collection
    */
   def createEdge(db : String, collectionName: String, edgeString : String, fromCollection : String, toCollection : String, from : String, to : String, createIfNotExists : Boolean = true): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "edge"
      val req = path.addQueryParameter("collection", collectionName).addQueryParameter("createCollection", createIfNotExists.toString).addQueryParameter("from",s"$fromCollection/$from").addQueryParameter("to",s"$toCollection/$to").setBody(edgeString).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right(result._key.get)
         }
      }
   }

   /*
   Replaces a edge with edgeString
    */
   def replaceEdge(db : String, collectionName: String, documentID : String, documentString : String): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "edge" / collectionName / documentID
      val req = path.setBody(documentString).PUT
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right(result._key.get)
         }
      }
   }

   /*
   Returns a list of all URI for all edges from the collection identified by collectionName.
    */
   def getAllEdges(db : String, collectionName : String, vertexStart : String, direction : Option[String] = None): Future[String] = {
      val path = arangoHost / "_db" / db / "_api" / "edges" / collectionName
      val request = path.addQueryParameter("vertex", vertexStart)
      val req = if(direction.nonEmpty) request.addQueryParameter("direction", direction.get).GET else request.GET
      Http(req OK as.String) map { x =>
         x
      }
   }

   /*
   Retrieve an edge using its unique URI:
    */
   def getEdge(db : String, collectionName : String, edgeId : String): Future[String] = {
      val path = arangoHost / "_db" / db / "_api" / "edge" / collectionName / edgeId
      val req = path.GET
      Http(req OK as.String)
   }

   /*
   Deletes an edge using its unique URI:
    */
   def deleteEdge(db : String, collectionName : String, edgeId : String): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "edge" / collectionName / edgeId
      val req = path.DELETE
      Http(req OK as.String).map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right("success")
         }
      }
   }
}