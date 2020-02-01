package proteus

import cats.effect.IO
import com.charlesahunt.proteus.client.GraphClient
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers._

class GraphClientTest extends AnyFunSpec {

  val testDB = "testGraph"
  val testEdgeCollection = "testEdgeCollection"
  val testVertexCollection = "testVertexCollection"
  val testVertexCollection2 = "testVertexCollection2"
  var createdEdgeKey = ""
  var createdVertexKey = ""

  val driver = new GraphClient[IO](testConfig, testDB)

  describe("==============\n| Graph Client Test |\n==============") {

    describe("Create Graph") {
      it("should create a graph") {
        val result = driver.createGraph(List())
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Vertex Collection") {
      it("should create vertex collection") {
        val result = driver.createVertexCollection(testVertexCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Vertex") {
      it("should create vertex in vertex collection") {
        val result = driver.createVertex(testVertexCollection, """{"test":"test"}""")
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Create Edge collection") {
      it("should create edge collection") {
        driver.createVertex(testVertexCollection, """{ "Hello": "World" }""")
        driver.createVertex(testVertexCollection2, """{ "Hello": "World" }""")

        val result = driver.createEdgeCollection(testEdgeCollection, List(testVertexCollection), List(testVertexCollection2))
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok.head.collection shouldEqual testEdgeCollection
        }
      }
    }

    describe("Create an edge in the edge collection") {
      it("should create edge definition") {
        val vert1 = driver.createVertex(testVertexCollection, """{ "Hello": "World" }""").unsafeRunSync()
        val vert2 = driver.createVertex(testVertexCollection2, """{ "Hello": "World" }""").unsafeRunSync()

        val vert1Edge = vert1 match {
          case Left(err) => fail(err)
          case Right(edge) => edge
        }

        val vert2Edge = vert2 match {
          case Left(err) => fail(err)
          case Right(edge) => edge
        }

        createdVertexKey = vert1Edge._key
        val result = driver.createEdge(testEdgeCollection, "test", vert1Edge._id, vert2Edge._id)

        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(edge) =>
            createdEdgeKey = edge._key
            edge._id.length should be > 0
        }

      }
    }

    describe("Delete an edge") {
      it("should remove an edge") {
        val result = driver.deleteEdge(testEdgeCollection, createdEdgeKey)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an vertex") {
      it("should remove a vertex") {
        val result = driver.deleteVertex(testVertexCollection, createdVertexKey)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an edge collection") {
      it("should remove an edge collection") {
        val result = driver.deleteEdgeCollection(testEdgeCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Delete an vertex collection") {
      it("should remove a vertex collection") {
        val result = driver.deleteVertexCollection(testVertexCollection)
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }

    describe("Drop Graph") {
      it("should drop the graph") {
        val result = driver.dropGraph
        result.unsafeRunSync() match {
          case Left(err) => fail(err)
          case Right(ok) => ok
        }
      }
    }
  }
}


