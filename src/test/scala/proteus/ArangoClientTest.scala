package proteus

import com.cornfluence.proteus.{ArangoClient, User}
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

//    describe("Authenticate with Arango") {
//      it("should return a JWT") {
//        val result = driver.auth("root", "")
//        val res = Await.result(result, 5 second)
//        res match {
//          case Left(err) => fail(err.getMessage)
//          case Right(ok) => ok
//        }
//      }
//    }

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

  }
}


