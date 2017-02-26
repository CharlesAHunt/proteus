package com.cornfluence.proteus

import com.cornfluence.proteus.models.{ReadAllDocumentKeys, ResultList, ResultMessage}
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
     val response = auth(Http(s"$arangoHost/$api/document/$collectionName").postData(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
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
     val response = auth(Http(s"$arangoHost/$api/document/$collectionName/$documentID").put(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
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
     val collection = ReadAllDocumentKeys(collectionName)
     val response = auth(Http(s"$arangoHost/$api/simple/all-keys")).put(collection.asJson.noSpaces).asString
     decode[ResultList](response.body) match {
       case Right(ok) => Right(ok.result)
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

   /*
   Retrieve a document using its unique URI:

   It is unfortunate that this response from Arango has the result structured the way it does, as I seem to be forced
   to return a String as the driver cannot predict the structure of the returned JSON
    */
   def getDocument(db : String, collectionName : String, documentID : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/$api/document/$collectionName/$documentID")).asString
     Right(response.body)
   }

   /*
   Deletes a document using its unique URI:
    */
   def deleteDocument(db : String, collectionName : String, documentID : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/$api/document/$collectionName/$documentID").method(DELETE)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
         else Right("success")
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }
}