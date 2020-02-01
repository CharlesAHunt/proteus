package proteus

import cats.effect.IO
import com.charlesahunt.proteus.client.DocumentClient
import com.charlesahunt.proteus.models.User
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class DocumentClientTest extends AnyFunSpec {

  val testDB = "testDocumentClient"
  val testCollection = "testDocumentClientCollection"
  var testDocID = ""
  val driver = new DocumentClient[IO](testConfig, testDB)

  describe("==============\n| Document Client Test |\n==============") {

    describe("Create Database") {
      it("should create new Database") {
        val result = driver.createDatabase(testDB, Some(List(User("charles", "password"))))
        result.unsafeRunSync() match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Collection") {
      it("should create a collection") {
        val result = driver.createCollection(testDB, testCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok.name.get shouldEqual testCollection
        }
      }
    }

    describe("Create Document") {
      it("should create document in test collection") {
        val result = driver.createDocument(testCollection, """{ "Hello": "World" }""")
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toLong should be > 0L
            testDocID = ok
        }
      }
    }

    describe("Retrieve All Documents") {
      it("should retrieve all documents in test collection") {
        val result = driver.getAllDocuments(testCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok.head should include(s"/_api/document/$testCollection/")
        }
      }
    }
    describe("Retrieve one document by handle") {
      it("should retrieve one document from the test collection") {
        val result = driver.getDocument(testCollection, testDocID)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toString should include(s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID""")
            ok.toString should include("""Hello":"World"}""")
        }
      }
    }
    describe("Replace one document by handle") {
      it("should replace one document from the test collection") {
        val result = driver.replaceDocument(testCollection, testDocID,"""{ "Hello": "Arango" }""")
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok should include(testDocID)
        }
      }
    }
    describe("Ensure replaced document has changed") {
      it("replaced document should have changed in the test collection") {
        val result = driver.getDocument(testCollection, testDocID)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toString should include(s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID""")
            ok.toString should include("""Hello":"Arango"}""")
        }
      }
    }
    describe("Remove a document by handle") {
      it("should remove one document from the test collection") {
        val result = driver.deleteDocument(testCollection, testDocID)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
    describe("Drop Collection") {
      it("should drop a collection") {
        val result = driver.dropCollection(testDB, testCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok.error shouldEqual false
        }
      }
    }
    describe("Delete Database") {
      it("should delete Database") {
        val result = driver.deleteDatabase(testDB)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
  }
}

