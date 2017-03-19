package com.cornfluence.proteus

import com.cornfluence.proteus.models.{CollectionName, ResultList, ResultMessage}
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

  /**
    * Creates a new document in the collection named collection
    *
    * @param dbName
    * @param collectionName
    * @param documentString
    * @return document key
    */
   def createDocument(
     dbName : String,
     collectionName: String,
     documentString : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/$db/$dbName/$api/document/$collectionName").postData(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
         else ok._key.toRight[Throwable](new Exception("Document key is missing"))
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

  /**
    * Replaces a document with documentString
    *
    * @param dbName
    * @param collectionName
    * @param documentID
    * @param documentString
    * @return document key
    */
   def replaceDocument(dbName : String, collectionName: String, documentID : String, documentString : String)
   : Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/$db/$dbName/$api/document/$collectionName/$documentID").put(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
         else ok._key.toRight[Throwable](new Exception("Document key is missing"))
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

  /**
    * Returns a list of all URI for all documents from the collection identified by collectionName.
    *
    * @param dbName
    * @param collectionName
    * @return
    */
   def getAllDocuments(dbName : String, collectionName : String): Future[Either[Throwable, List[String]]] = Future {
     val collection = CollectionName(collectionName)
     val response = auth(Http(s"$arangoHost/$db/$dbName/$api/simple/all-keys")).put(collection.asJson.noSpaces).asString
     decode[ResultList](response.body) match {
       case Right(ok) => Right(ok.result)
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

  /**
    * Retrieve a document using its unique URI:
    *
    * @param dbName
    * @param collectionName
    * @param documentID
    * @return document JSON body
    */
   def getDocument(dbName : String, collectionName : String, documentID : String): Future[Either[Throwable, String]] = Future {
     val response = auth(Http(s"$arangoHost/$db/$dbName/$api/document/$collectionName/$documentID")).asString
     Right(response.body)
   }

  /**
    * Deletes a document using its unique URI
    *
    * @param dbName
    * @param collectionName
    * @param documentID
    * @return
    */
   def deleteDocument(dbName : String, collectionName : String, documentID : String): Future[Either[Throwable, Unit]] = Future {
     val response = auth(Http(s"$arangoHost/$db/$dbName/$api/document/$collectionName/$documentID").method(DELETE)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(ok.error.getOrElse(false)) Left(new Exception(errorMessage(ok.errorMessage)))
         else Right(())
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }
}