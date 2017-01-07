package proteus

import com.cornfluence.proteus.{DocumentClient, User}
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import scala.language.postfixOps

import scala.concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._

class DocumentClientTest extends FunSpec {

  val testDB = "testDocumentClient"
  val testCollection = "testDocumentClientCollection"
  var testDocID = ""
  val driver = DocumentClient(name = testDB)

  describe("==============\n| Document Client Test |\n==============") {
    describe("Create Database") {
      it("should create new Database") {
        val result = driver.createDatabase(testDB, Some(List(User("charles", "password"))))
        Await.result(result, 5.seconds) match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
    describe("Get Databases") {
      it("should properly retrieve all databases") {
        val result = driver.getDatabaseList
        Await.result(result, 5.seconds) match {
          case Left(err) => fail(err)
          case Right(ok) => assert(ok.nonEmpty)
        }
      }
    }

    describe("Create Document") {
      it("should create document in test collection") {
        val result = driver.createDocument(testDB, testCollection,"""{ "Hello": "World" }""")

        val res = Await.result(result, 5 second)
        testDocID = res.right.get

        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok.toLong should be > 0L
        }
      }
    }
    describe("Retrieve All Documents") {
      it("should retrieve all documents in test collection") {
        val result = driver.getAllDocuments(testDB, testCollection)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok.head should include(s"/_api/document/$testCollection/")
        }
      }
    }
    describe("Retrieve one document by handle") {
      it("should retrieve one document from the test collection") {
        Thread.sleep(500)
        val result = driver.getDocument(testDB, testCollection, testDocID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok should include(s"""{"Hello":"World","_id":"$testCollection/$testDocID""")
        }
      }
    }
    describe("Replace one document by handle") {
      it("should replace one document from the test collection") {
        val result = driver.replaceDocument(testDB, testCollection, testDocID,"""{ "Hello": "Arango" }""")
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok should include(testDocID)
        }
      }
    }
    describe("Ensure replaced document has changed") {
      it("replaced document should have changed in the test collection") {
        val result = driver.getDocument(testDB, testCollection, testDocID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok should include(s"""{"Hello":"Arango","_id":"$testCollection/""" + testDocID)
        }
      }
    }
    describe("Remove a document by handle") {
      it("should remove one document from the test collection") {
        val result = driver.deleteDocument(testDB, testCollection, testDocID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok should include("success")
        }
      }
    }
    describe("Delete Database") {
      it("should delete Database") {
        val result = driver.deleteDatabase(testDB)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
  }
}

