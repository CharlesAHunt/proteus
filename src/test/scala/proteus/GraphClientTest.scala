package proteus

import com.cornfluence.proteus.models.User
import com.cornfluence.proteus.GraphClient
import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class GraphClientTest extends FunSpec {

  val testDB = "testGraph"
  val testEdgeCollection = "testEdgeCollection"
  val testVertexCollection = "testVertexCollection"
  var testDocID = ""
  var fromID = ""

  val driver = new GraphClient(databaseName = testDB)

  describe("==============\n| Graph Client Test |\n==============") {

    describe("Create Graph") {
      it("should create a graph") {
        val result = Await.result(driver.createGraph(testDB, List()), 5 second)
        result match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Vertex") {
      it("should create edge in test collection") {
        val result = Await.result(driver.createVertexCollection(testDB, testVertexCollection), 5 second)
        result match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

//    describe("Create Edge") {
//      it("should create edge in test collection") {
//        val result1 = driver.createVertex(testDB, testVertexCollection, """{ "Hello": "World" }""")
//        val res1 = Await.result(result1, 5 second)
//        println("RES!:  "+res1)
//        fromID = res1.right.get
//
//        val result2 = driver.createVertex(testDB, testVertexCollection, """{ "Hello": "World" }""")
//        val res2 = Await.result(result2, 5 second)
//        val toID = res2.right.get
//
//        val result3 = driver.createEdge(testEdgeCollection, fromID, toID)
//
//        val res3 = Await.result(result3, 5 second)
//        testDocID = res3.right.get
//        val res4 = Await.result(result3, 5 second)
//
//        res4 match {
//          case Left(err) => fail(err)
//          case Right(ok) => ok.toLong should be > 0L
//        }
//      }
//    }
//    describe("Retrieve All Edges") {
//      it("should retrieve all edges in test collection") {
//        val result = driver.getAllEdges(testDB, testEdgeCollection, s"$testVertexCollection/$fromID")
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err)
//          case Right(ok) => //ok should include(testVertexCollection)
//        }
//      }
//    }
//    describe("Retrieve one edge by handle") {
//      it("should retrieve one edge from the test collection") {
//        val result = driver.getEdge(testDB, testEdgeCollection, testDocID)
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err)
//          case Right(ok) => //ok should include( s"""{"Hello":"World","_id":"$testEdgeCollection/""" + testDocID)
//        }
//      }
//    }
//    describe("Replace one edge by handle") {
//      it("should replace one edge from the test collection") {
//        val result = driver.replaceEdge(testDB, testEdgeCollection, testDocID, """{ "Hello": "Arango" }""")
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err)
//          case Right(ok) => res.right.get should include(testDocID)
//        }
//      }
//    }
//    describe("Ensure replaced edge has changed") {
//      it("replaced edge should have changed in the test collection") {
//        val result = driver.getEdge(testDB, testEdgeCollection, testDocID)
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err)
//          case Right(ok) => //ok should include( s"""{"Hello":"Arango","_id":"$testEdgeCollection/""" + testDocID)
//        }
//      }
//    }
//    describe("Remove a edge by handle") {
//      it("should remove one edge from the test collection") {
//        val result = driver.deleteEdge(testDB, testEdgeCollection, testDocID)
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err)
//          case Right(ok) => ok
//        }
//      }
//    }

    describe("Drop Graph") {
      it("should drop the graph") {
        val result = Await.result(driver.dropGraph(testDB), 5 second)
        result match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
  }
}


