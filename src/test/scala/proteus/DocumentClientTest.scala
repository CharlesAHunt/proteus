package proteus

import com.cornfluence.proteus.models.User
import com.cornfluence.proteus.DocumentClient
import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps
import scala.concurrent.{Await, ExecutionContext}
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
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
        }
      }
    }
    describe("Get Databases") {
      it("should properly retrieve all databases") {
        val result = driver.getDatabaseList
        Await.result(result, 5.seconds) match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => assert(ok.nonEmpty)
        }
      }
    }

    describe("Create Collection") {
      it("should create a collection") {
        val result = driver.createCollection(testDB, testCollection)

        val res = Await.result(result, 5 second)

        res match {
          case Left(err) => fail(err)
          case Right(ok) => res.right.get.name.get shouldEqual testCollection
        }
      }
    }

    describe("Create Document") {
      it("should create document in test collection") {
        val result = driver.createDocument(testDB, testCollection, """{ "Hello": "World" }""")

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
        val result = driver.getDocument(testDB, testCollection, testDocID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toString should include(s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID""")
            ok.toString should include("""Hello":"World"}""")
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
          case Right(ok) =>
            ok.toString should include(s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID""")
            ok.toString should include("""Hello":"Arango"}""")
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
    describe("Drop Collection") {
      it("should drop a collection") {
        val result = driver.dropCollection(testDB, testCollection)

        val res = Await.result(result, 5 second)

        res match {
          case Left(err) => fail(err)
          case Right(ok) => res.right.get.error shouldEqual false
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

