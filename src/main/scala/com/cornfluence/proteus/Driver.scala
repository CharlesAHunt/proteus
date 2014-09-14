package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

class Driver(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   val arrangoHost = if (https) host(hostMachine, port).secure else host(hostMachine, port)

   val connection = None

   def isConnection = connection.nonEmpty

   def getDatabases: Future[List[String]] = {
      val path = arrangoHost / "_api" / "database"
      val req = path.GET
      Http(req OK as.String) map {x => ScalaJack.read[Result](x).result}
   }
}

case class Result(
   result: List[String],
   error: Boolean,
   code: Int
)