package proteus

import com.cornfluence.proteus.{User, Driver}
import org.scalatest.FunSpec
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

class DriverTest extends FunSpec {

   describe("==============\n| Driver Test |\n==============") {
      describe("Get Databases") {
         it("should properly retrieve all databases in Arango") {

            val driver = new Driver(databaseName = "test")
            val result = driver.getDatabaseList
            result.onComplete { x => assert(x.getOrElse(List.empty).nonEmpty)}
         }
      }
      describe("Create Database") {
//         it("should create new Database") {
//
//            val driver = new Driver(databaseName = "test")
//            val result = driver.createDatabase("test", Some(List(User("charles", "password"))))
//            result.onComplete {
//               case Success(res) => assert(res)
//               case Failure(t) => fail()
//            }
//         }
      }
      describe("Create Document") {
         it("should create document in test collection") {

            val driver = new Driver(databaseName = "test")
            val result = driver.createDocument("test","testDocument","testCollection","""{ "Hello": "World" }""")

            result.onComplete {
               case Success(res) => println(res)
               case Failure(t) => t.printStackTrace()
            }
         }
      }
      describe("Delete Database") {
         it("should delete Database") {

//            val driver = new Driver(databaseName = "test")
//            val result = driver.deleteDatabase("test")
//            result.onComplete {
//               case Success(res) => assert(res)
//               case Failure(t) => fail()
//            }
         }
      }
   }
}

