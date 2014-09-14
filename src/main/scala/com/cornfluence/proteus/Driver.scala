package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

class Driver(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   val arangoHost = if (https) host(hostMachine, port).secure else host(hostMachine, port)

   val connection = None

   def isConnection = connection.nonEmpty

   def getDatabases: Future[List[String]] = {
      val path = arangoHost / "_api" / "database"
      val req = path.GET
      Http(req OK as.String) map {x => ScalaJack.read[ResultList](x).result}
   }

   def createDatabase(dbName:String, users : Option[List[User]]): Future[Boolean] = {
      val path = arangoHost / "_api" / "database"
      val req = path.setBody(ScalaJack.render(Database(dbName, users))).POST
      Http(req OK as.String) map {x => ScalaJack.read[ResultBoolean](x).result}
   }

   def deleteDatabase(dbName:String): Future[Boolean] = {
      val path = arangoHost / "_api" / "database" / dbName
      val req = path.DELETE
      Http(req OK as.String) map {x => ScalaJack.read[ResultBoolean](x).result}
   }
}

case class ResultList(
   result: List[String],
   error: Boolean,
   code: Int
)

case class ResultBoolean(
   result: Boolean,
   error: Boolean,
   code: Int
)

case class Database(
   name: String,
   users: Option[List[User]]
)

case class User(
   username: String,
   passwd: String,
   active: Boolean = true,
   extra: Option[String] = None
)