package proteus

import com.charlesahunt.proteus.GraphClient
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
  var createdEdgeKey = ""
  var createdVertexKey = ""

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

        createdVertexKey = vert._key

        val result = driver.createEdge(testDB, testEdgeCollection, "test", vert._id, vert2._id)
        val res = Await.result(result, 5 second)

        res match {
          case Left(err) => fail(err)
          case Right(edge) =>
            createdEdgeKey = edge._key
            edge._id.length should be > 0
        }
      }
    }

    describe("Delete an edge") {
      it("should remove an edge") {

        val result = driver.deleteEdge(testDB, testEdgeCollection, createdEdgeKey)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an vertex") {
      it("should remove a vertex") {
        val result = driver.deleteVertex(testDB, testVertexCollection, createdVertexKey)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an edge collection") {
      it("should remove an edge collection") {
        val result = driver.deleteEdgeCollection(testDB, testEdgeCollection)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an vertex collection") {
      it("should remove a vertex collection") {
        val result = driver.deleteVertexCollection(testDB, testVertexCollection)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

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


