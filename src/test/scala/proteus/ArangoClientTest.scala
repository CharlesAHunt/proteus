package proteus

import cats.effect.IO
import com.charlesahunt.proteus.client.ArangoClient
import com.charlesahunt.proteus.models.User
import org.scalatest.funspec.AnyFunSpec

import scala.language.postfixOps

class ArangoClientTest extends AnyFunSpec {

  val testDB = "testDocumentClient"
  val driver = new ArangoClient[IO](testConfig)

  describe("==============\n| Arango Client Test |\n==============") {

    describe("Create Database") {
      it("should create new Database") {
        val result = driver.createDatabase(testDB, Some(List(User("user", "password"))))
        result.unsafeRunSync() match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
        }
      }
    }

    describe("Get Databases") {
      it("should properly retrieve all databases") {
        val result = driver.getDatabaseList
        result.unsafeRunSync() match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => assert(ok.nonEmpty)
        }
      }
    }

    describe("Get current database") {
      it("should properly retrieve the current database") {
        val result = driver.getCurrentDatabase
        result.unsafeRunSync() match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => assert(ok.result.name.nonEmpty)
        }
      }
    }

    describe("Delete Database") {
      it("should delete the Database") {
        val result = driver.deleteDatabase(testDB)
        result.unsafeRunSync() match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
        }
      }
    }

  }
}


