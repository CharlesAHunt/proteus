package proteus

import com.cornfluence.proteus.ArangoClient
import com.cornfluence.proteus.models.User
import org.scalatest.FunSpec
import org.scalatest.Matchers._

import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class ArangoClientTest extends FunSpec {

  val testDB = "testDocumentClient"
  val driver = new ArangoClient(databaseName = testDB)


  describe("==============\n| Arango Client Test |\n==============") {

    describe("Create Database") {
      it("should create new Database") {
        val result = driver.createDatabase(testDB, Some(List(User("user", "password"))))
        val res = Await.result(result, 5 second)
        res match {
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

    describe("Get current database") {
      it("should properly retrieve the current database") {
        val result = driver.getCurrentDatabase
        Await.result(result, 5.seconds) match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => assert(ok.result.name.nonEmpty)
        }
      }
    }

    describe("Delete Database") {
      it("should delete the Database") {
        val result = driver.deleteDatabase(testDB)
        val res = Await.result(result, 5 second)
        res match {
          case Left(err) => fail(err.getMessage)
          case Right(ok) => ok
        }
      }
    }

  }
}


