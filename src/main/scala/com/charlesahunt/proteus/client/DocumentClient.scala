package com.charlesahunt.proteus.client

import cats.effect.Sync
import com.charlesahunt.proteus.config.ProteusConfig
import com.charlesahunt.proteus.models.{CollectionName, ResultList, ResultMessage}
import com.charlesahunt.proteus.{DELETE, api, db, error, errorMessage, isError}
import com.typesafe.scalalogging.Logger
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import scalaj.http._

/**
  * Manages Document API operations
  *
  * @param databaseName
  */
class DocumentClient[F[_]](val config: ProteusConfig, val databaseName: String)(implicit override val sync: Sync[F])
  extends ArangoClient[F](config: ProteusConfig)(sync: Sync[F]) {

  private val logger = Logger[DocumentClient[F]]

  /**
    * Creates a new document in the collection named collection
    *
    * @param collectionName
    * @param documentString
    * @return document key
    */
   def createDocument(
     collectionName: String,
     documentString : String): F[Either[Throwable, String]] = sync.delay {
     val response = postAuth(Http(s"$arangoHost/$db/$databaseName/$api/document/$collectionName").postData(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(isError(ok)) error(errorMessage(ok.errorMessage))
         else ok._key.toRight[Throwable](new Exception("Document key is missing"))
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

  /**
    * Replaces a document with documentString
    *
    * @param collectionName
    * @param documentID
    * @param documentString
    * @return document key
    */
   def replaceDocument(collectionName: String, documentID : String, documentString : String)
   : F[Either[Throwable, String]] = sync.delay {
     val response = postAuth(Http(s"$arangoHost/$db/$databaseName/$api/document/$collectionName/$documentID").put(documentString)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(isError(ok)) error(errorMessage(ok.errorMessage))
         else ok._key.toRight[Throwable](new Exception("Document key is missing"))
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }

  /**
    * Returns a list of all URI for all documents from the collection identified by collectionName.
    *
    * @param collectionName
    * @return
    */
   def getAllDocuments(collectionName : String): F[Either[Throwable, List[String]]] = sync.delay {
     val collection = CollectionName(collectionName)
     val response = postAuth(Http(s"$arangoHost/$db/$databaseName/$api/simple/all-keys")).put(collection.asJson.noSpaces).asString
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
    * @param collectionName
    * @param documentID
    * @return document JSON body
    */
   def getDocument(collectionName : String, documentID : String): F[Either[Throwable, String]] = sync.delay {
     val response = postAuth(Http(s"$arangoHost/$db/$databaseName/$api/document/$collectionName/$documentID")).asString
     Right(response.body)
   }

  /**
    * Deletes a document using its unique URI
    *
    * @param collectionName
    * @param documentID
    * @return
    */
   def deleteDocument(collectionName : String, documentID : String): F[Either[Throwable, Unit]] = sync.delay {
     val response = postAuth(Http(s"$arangoHost/$db/$databaseName/$api/document/$collectionName/$documentID").method(DELETE)).asString
     decode[ResultMessage](response.body) match {
       case Right(ok) =>
         if(isError(ok)) error(errorMessage(ok.errorMessage))
         else Right(())
       case Left(error) =>
         logger.error(error.getMessage)
         Left(error)
     }
   }
}