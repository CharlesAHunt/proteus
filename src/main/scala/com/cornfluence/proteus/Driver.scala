package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

class Driver(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   val arangoHost = if (https) host(hostMachine, port).secure else host(hostMachine, port)

   val connection = None

   def isConnection = connection.nonEmpty

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

   def createDocument(db : String, collection: String, body : String): Future[String] = {
      val path = arangoHost / "_db" / db / "_api" / "document"
      val req = path.addQueryParameter("collection", collection).addQueryParameter("createCollection","true").setBody(body).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => "ok"
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

   def removeDocument(db : String, documentName:String): Future[String] = {
      val path = arangoHost / "_db" / db /"_api" / "document"
      val req = path.DELETE
      Http(req OK as.String) map {x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => throw new Exception(result.errorMessage.get)
            case false => "ok"
         }
      }
   }
}

case class ResultList(
   result: List[String],
   error: Boolean,
   code: Int
)

case class ResultMessage(
   error: Boolean,
   errorMessage: Option[String],
   result: Option[Boolean],
   code: Option[Int],
   errorNum : Option[Int],
   _id: Option[String],
   _rev: Option[String],
   _key: Option[String]
)

case class Database(
   name: String,
   users: Option[List[User]]
)

case class Documents(
   documents: List[String]
)

case class User(
   username: String,
   passwd: String,
   active: Boolean = true,
   extra: Option[String] = None
)