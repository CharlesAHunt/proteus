package com.cornfluence.proteus

import co.blocke.scalajack.ScalaJack
import dispatch._, Defaults._

object DocumentClient {
   def apply(name : String) = new DocumentClient(databaseName = name)
   def apply(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) = new DocumentClient(hostMachine,port, https, databaseName)
}

class DocumentClient(hostMachine: String = "localhost", port: Int = 8529, https: Boolean = false, databaseName: String) extends ArangoClient(hostMachine, port, https, databaseName) {

   /*
   Creates a new document in the collection named collection
    */
   def createDocument(db : String, collectionName: String, documentString : String, createIfNotExists : Boolean = true): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "document"
      val req = path.addQueryParameter("collection", collectionName).addQueryParameter("createCollection", createIfNotExists.toString).setBody(documentString).POST
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right(result._key.get)
         }
      }
   }

   /*
   Replaces a document with documentString
    */
   def replaceDocument(db : String, collectionName: String, documentID : String, documentString : String): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "document" / collectionName / documentID
      val req = path.setBody(documentString).PUT
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right(result._key.get)
         }
      }
   }

   /*
   Returns a list of all URI for all documents from the collection identified by collectionName.
    */
   def getAllDocuments(db : String, collectionName : String): Future[List[String]] = {
      val path = arangoHost / "_db" / db / "_api" / "document"
      val req = path.addQueryParameter("collection", collectionName).GET
      Http(req OK as.String) map { x =>
         val result = ScalaJack.read[Documents](x)
         result.documents
      }
   }

   /*
   Retrieve a document using its unique URI:
    */
   def getDocument(db : String, collectionName : String, documentID : String): Future[String] = {
      val path = arangoHost / "_db" / db / "_api" / "document" / collectionName / documentID
      val req = path.GET
      Http(req OK as.String)
   }

   /*
   Deletes a document using its unique URI:
    */
   def deleteDocument(db : String, collectionName : String, documentID : String): Future[Either[Error,String]] = {
      val path = arangoHost / "_db" / db / "_api" / "document" / collectionName / documentID
      val req = path.DELETE
      Http(req OK as.String).map { x =>
         val result = ScalaJack.read[ResultMessage](x)
         result.error match {
            case true => Left(Error(result.errorMessage.get))
            case false => Right("success")
         }
      }
   }
}