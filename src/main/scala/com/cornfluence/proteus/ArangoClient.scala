package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

object ArangoClient {
   def apply(name : String) = new ArangoClient(databaseName = name)
   def apply(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) = new ArangoClient(hostMachine,port, https, databaseName)
}

/*
*  ArangoDB Driver: Defaults to localhost:8529 unless otherwise specified
*/
class ArangoClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   val arangoHost = if (https) host(hostMachine, port).secure else host(hostMachine, port)

   /*
   Retrieves the list of all existing databases
    */
   def getDatabaseList: Future[List[String]] = {
      val path = arangoHost / "_api" / "database"
      val req = path.GET
      Http(req OK as.String) map {x => ScalaJack.read[ResultList](x).result}
   }

   /*
   Creates a new database
    */
   def createDatabase(dbName:String, users : Option[List[User]]): Future[Either[Error,String]] = {
      val path = arangoHost / "_api" / "database"
      val req = path.setBody(ScalaJack.render(Database(dbName, users))).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right("success")
         }
      }
   }

   /*
   Deletes the database along with all data stored in it
    */
   def deleteDatabase(dbName:String): Future[Either[Error,String]] = {
      val path = arangoHost / "_api" / "database" / dbName
      val req = path.DELETE
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right("success")
         }
      }
   }
}