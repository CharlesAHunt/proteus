package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

/*
*  ArangoDB Driver: Defaults to localhost:8529 unless otherwise specified
*/
class Driver(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   val arangoHost = if (https) host(hostMachine, port).secure else host(hostMachine, port)

   def getDatabaseList: Future[List[String]] = {
      val path = arangoHost / "_api" / "database"
      val req = path.GET
      Http(req OK as.String) map {x => ScalaJack.read[ResultList](x).result}
   }

   def createDatabase(dbName:String, users : Option[List[User]]): Future[String] = {
      val path = arangoHost / "_api" / "database"
      val req = path.setBody(ScalaJack.render(Database(dbName, users))).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => "ok"
         }
      }
   }

   def deleteDatabase(dbName:String): Future[String] = {
      val path = arangoHost / "_api" / "database" / dbName
      val req = path.DELETE
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => "ok"
         }
      }
   }

   def createDocument(db : String, collection: String, documentString : String, createIfNotExists : Boolean = true): Future[String] = {
      val path = arangoHost / "_db" / db / "_api" / "document"
      val req = path.addQueryParameter("collection", collection).addQueryParameter("createCollection", createIfNotExists.toString).setBody(documentString).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => result._key.get
         }
      }
   }

   def getAllDocuments(db : String, collectionName : String): Future[List[String]] = {
      val path = arangoHost / "_db" / db /"_api" / "document"
      val req = path.addQueryParameter("collection", collectionName).GET
      Http(req OK as.String) map {x =>
         val result = ScalaJack.read[Documents](x)
         result.documents
      }
   }

   def getDocument(db : String, collectionName : String, documentID : String): Future[String] = {
      val path = arangoHost / "_db" / db /"_api" / "document" / collectionName / documentID
      val req = path.GET
      Http(req OK as.String)
   }

   def removeDocument(db : String, collectionName : String, documentID : String): Future[String] = {
      val path = arangoHost / "_db" / db /"_api" / "document" / collectionName / documentID
      val req = path.DELETE
      Http(req OK as.String).map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => "ok"
         }
      }
   }
}