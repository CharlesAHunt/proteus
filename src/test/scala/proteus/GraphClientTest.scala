package proteus

import com.cornfluence.proteus.GraphClient
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.scalatest.Matchers._

import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class GraphClientTest extends FunSpec {

  val testDB = "testGraph"
  val testEdgeCollection = "testEdgeCollection"
  val testVertexCollection = "testVertexCollection"
  val testVertexCollection2 = "testVertexCollection2"
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

    describe("Create Vertex Collection") {
      it("should create vertex collection") {
        val result = Await.result(driver.createVertexCollection(testDB, testVertexCollection), 5 second)
        result match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Vertex") {
      it("should create vertex in vertex collection") {
        val result = Await.result(driver.createVertex(testDB, testVertexCollection, """{"test":"test"}"""), 5 second)
        result match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Edge collection") {
      it("should create edge collection") {
        Await.result(driver.createVertex(testDB, testVertexCollection, """{ "Hello": "World" }"""), 5 second)
        Await.result(driver.createVertex(testDB, testVertexCollection2, """{ "Hello": "World" }"""), 5 second)

        val result = driver.createEdgeCollection(testDB, testEdgeCollection, List(testVertexCollection), List(testVertexCollection2))
        val res = Await.result(result, 5 second)

        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok.head.collection shouldEqual testEdgeCollection
        }
      }
    }

    describe("Create an edge in the edge collection") {
      it("should create edge definition") {
        val vert = Await.result(driver.createVertex(testDB, testVertexCollection, """{ "Hello": "World" }"""), 5 second).toOption.get
        val vert2 = Await.result(driver.createVertex(testDB, testVertexCollection2, """{ "Hello": "World" }"""), 5 second).toOption.get

        val result = driver.createEdge(testDB, testEdgeCollection, "test", vert._id, vert2._id)
        val res = Await.result(result, 5 second)

        res match {
          case Left(err) => fail(err)
          case Right(edge) => edge._id.length should be > 0
        }
      }
    }

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


