package proteus

import com.charlesahunt.proteus.DocumentClient
import com.charlesahunt.proteus.models.User
import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.language.postfixOps
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

class DocumentClientTest extends FunSpec {

  val testDB = "testDocumentClient"
  val testCollection = "testDocumentClientCollection"
  var testDocID = ""
  var testDocID2 = ""
  var testDocID3 = ""
  var cursorID = ""
  val driver = DocumentClient(name = testDB)

  describe("==============\n| Document Client Test |\n==============") {

    describe("Create Database") {
      it("should create new Database") {
        val result =
          driver.createDatabase(testDB, Some(List(User("charles", "password"))))
        Await.result(result, 5.seconds) match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
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
        val result = driver.createDocument(
          testDB,
          testCollection,
          """{ "title": "developer", "company":"Test", "email":"test@gmail.com" }"""
        )

        val res = Await.result(result, 5 second)
        testDocID = res.right.get

        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok.toLong should be > 0L
        }
      }
    }

    describe("Create second Document") {
      it("should create another document in test collection") {
        val result = driver.createDocument(
          testDB,
          testCollection,
          """{ "title": "developer", "company":"Test", "email":"abc@gmail.com" }"""
        )

        val res = Await.result(result, 5 second)
        testDocID2 = res.right.get

        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok.toLong should be > 0L
        }
      }
    }

    describe("Create a third Document") {
      it("should create 3rd document in test collection") {
        val result = driver.createDocument(
          testDB,
          testCollection,
          """{ "title": "developer", "company":"Test", "email":"xyz@gmail.com" }"""
        )

        val res = Await.result(result, 5 second)
        testDocID3 = res.right.get

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
          case Right(ok) =>
            println(res)
            ok.head should include(s"/_api/document/$testCollection/")
        }
      }
    }
    describe("Retrieve one or more document(s) via AQL") {
      it("should retrieve 2 documents from the test collection using AQL") {
        val query = s"""FOR u IN $testCollection LIMIT 99 RETURN u"""
        val q = com.charlesahunt.proteus.models
          .Query(query = query, count = true, batchSize = 2)
        val result = driver.getQueryResult(testDB, testCollection, q)
        //implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toString should include(
              s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID"""
            )
            val userS =
              """"email":"test@gmail.com""""
            ok.toString should include(userS)
            ok.toString should include(
              s"""{"_key":"$testDocID2","_id":"$testCollection/$testDocID2"""
            )
            val userS2 =
              """"email":"abc@gmail.com""""
            ok.toString should include(userS2)
            cursorID = com.charlesahunt.proteus.utils.Utils
              .getCursorID(res)
              .getOrElse("")
            cursorID.size should be > 0
        }
      }
    }

    describe("Retrieve final document via cursor ID") {
      it("should retrieve last document from the test collection") {
        val result =
          driver.getQueryPendingResult(testDB, testCollection, cursorID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) =>
            ok.toString should include(
              s"""{"_key":"$testDocID3","_id":"$testCollection/$testDocID3"""
            )

            val userS =
              """"email":"xyz@gmail.com""""
            ok.toString should include(userS)
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
            ok.toString should include(
              s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID"""
            )
            val userS =
              """"email":"test@gmail.com""""
            ok.toString should include(userS)
        }
      }
    }
    describe("Replace one document by handle") {
      it("should replace one document from the test collection") {
        val userS =
          """{ "title": "developer", "company":"Test", "email":"test@test.com" }"""
        val result = driver.replaceDocument(
          testDB,
          testCollection,
          testDocID,
          userS
        )
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
            ok.toString should include(
              s"""{"_key":"$testDocID","_id":"$testCollection/$testDocID"""
            )
            val userS = """"email":"test@test.com""""
            ok.toString should include(userS)
        }
      }
    }
    describe("Remove a document by handle") {
      it("should remove one document from the test collection") {
        val result = driver.deleteDocument(testDB, testCollection, testDocID)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
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
