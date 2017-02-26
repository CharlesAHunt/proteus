package com.cornfluence.proteus

import com.cornfluence.proteus.models.{Documents, ResultMessage}
import com.typesafe.scalalogging.Logger

import scala.concurrent.Future
import scalaj.http._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser.{decode, _}
import io.circe.syntax._

object DocumentClient {
   def apply(name : String) = new DocumentClient(databaseName = name)
   def apply(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) =
      new DocumentClient(host, port, https, databaseName)
}

class DocumentClient(host: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String)
  extends ArangoClient(host, port, https, databaseName) with Auth {

  private val logger = Logger[DocumentClient]

  /*
   Creates a new document in the collection named collection
    */
   def createDocument(db : String, collectionName: String, documentString : String, createIfNotExists : Boolean = true)
   : Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/_db/$db/$api/document/$collectionName").postData(documentString)).asString
     println("RESPONSE1: "+response.body)
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
         else Right(ok._key.get)
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

   /*
   Replaces a document with documentString
    */
   def replaceDocument(db : String, collectionName: String, documentID : String, documentString : String)
   : Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/_db/$db/$api/document/collectionName/$documentID")
       .put(documentString)).asString
     println("RESPONSE2: "+response.body)
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
         else Right(ok._key.get)
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

   /*
   Returns a list of all URI for all documents from the collection identified by collectionName.
    */
   def getAllDocuments(db : String, collectionName : String): Future[Either[Throwable, List[String]]] = Future {
     val response = auth(Http(s"$arangoHost/_db/$db/$api/document")
       .param("collection", collectionName)).asString
     println("RESPONSE3: "+response.body)
     decode[Documents](response.body) match {
       case Right(ok) => Right(ok.documents)
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

   /*
   Retrieve a document using its unique URI:
    */
   def getDocument(db : String, collectionName : String, documentID : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/_db/$db/$api/document/$collectionName/$documentID")).asString
     println("RESPONSE4: "+response.body)

     decode[Documents](response.body) match {
       case Right(ok) => Right(ok.documents.head) //TODO This probably is not right
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

   /*
   Deletes a document using its unique URI:
    */
   def deleteDocument(db : String, collectionName : String, documentID : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/_db/$db/$api/document/$collectionName/$documentID").method(DELETE)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error) Left(new Exception(errorMessage(ok.errorMessage)))
         else Right("success")
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }
}