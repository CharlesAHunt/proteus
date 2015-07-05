package proteus

import com.cornfluence.proteus.{DocumentClient, User}
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
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
            result.onComplete {
               case Success(res) => res.right.get shouldEqual "success"
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Get Databases") {
         it("should properly retrieve all databases") {
            val result = driver.getDatabaseList
            result.onComplete { x => assert(x.getOrElse(List.empty).nonEmpty)}
         }
      }

      describe("Create Document") {
         it("should create document in test collection") {
            val result = driver.createDocument(testDB,testCollection,"""{ "Hello": "World" }""")

            val res = Await.result(result, 5 second)
            testDocID = res.right.get

            result.onComplete {
               case Success(res) => res.right.get.toLong should be > 0L ;
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Retrieve All Documents") {
         it("should retrieve all documents in test collection") {
            val result = driver.getAllDocuments(testDB, testCollection)

            result.onComplete {
               case Success(res) => res.head should include (s"/_api/document/$testCollection/")
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Retrieve one document by handle") {
         it("should retrieve one document from the test collection") {
            Thread.sleep(500)
            val result = driver.getDocument(testDB, testCollection, testDocID)

            result.onComplete {
               case Success(res) => res should include (s"""{"Hello":"World","_id":"$testCollection/$testDocID""")
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Replace one document by handle") {
         it("should replace one document from the test collection") {
            val result = driver.replaceDocument(testDB, testCollection, testDocID,"""{ "Hello": "Arango" }""")

            result.onComplete {
               case Success(res) => res.right.get should include (testDocID)
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Ensure replaced document has changed") {
         it("replaced document should have changed in the test collection") {
            val result = driver.getDocument(testDB, testCollection, testDocID)

            result.onComplete {
               case Success(res) => res should include (s"""{"Hello":"Arango","_id":"$testCollection/"""+testDocID)
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Remove a document by handle") {
         it("should remove one document from the test collection") {
            val result = driver.deleteDocument(testDB, testCollection,testDocID)

            result.onComplete {
               case Success(res) => res.right.get should include ("success")
               case Failure(t) => fail(t)
            }
         }
      }
      describe("Delete Database") {
         it("should delete Database") {
            val result = driver.deleteDatabase(testDB)
            result.onComplete {
               case Success(res) => res.right.get should include("success")
               case Failure(t) => fail()
            }
         }
      }
   }
}

