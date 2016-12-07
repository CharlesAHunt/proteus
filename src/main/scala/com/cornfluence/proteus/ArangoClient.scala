package com.cornfluence.proteus

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

object ArangoClient {
   def apply(name : String) = new ArangoClient(databaseName = name)
   def apply(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) = new ArangoClient(hostMachine,port, https, databaseName)
}

/**
  *
  * @param hostMachine
  * @param port
  * @param https
  * @param databaseName
  */
class ArangoClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) {

   private val arangoHost = if (https) s"https://$hostMachine:$port" else s"http://$hostMachine:$port"
   private val api = "_api"
   private val database = "database"

  /*
   Retrieves the list of all existing databases
    */
   def getDatabaseList: Future[List[String]] = {
     val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database").asString
     decode[ResultList](response.body)
   }

   /*
   Creates a new database
    */
   def createDatabase(dbName:String, users : Option[List[User]]): Future[Either[Error,String]] = {
     val postData = Database(dbName, users)
     val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database").postData(postData.asJson.noSpaces).asString
     val result = decode[ResultMessage](response.body)

     result.error match {
        case true => Left(Error(result.errorMessage.get))
        case false => Right("success")
     }
   }

   /*
   Deletes the database along with all data stored in it
    */
   def deleteDatabase(dbName:String): Future[Either[Error,String]] = {
     val response: HttpResponse[String] = Http(s"$arangoHost/$api/$database/$dbName").method("DELETE").asString
       val result = response TO ResultMessage
       result.error match {
          case true => Left(Error(result.errorMessage.get))
          case false => Right("success")
       }

   }
}