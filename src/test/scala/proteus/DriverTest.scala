package proteus

import com.cornfluence.proteus.Driver
import org.scalatest.FunSpec
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

class DriverTest extends FunSpec {

  describe("==============\n| Driver Test |\n==============") {
    describe("Get Databases") {
      it("should properly retrieve all databases in Arango") {

        val driver = new Driver(databaseName = "test")
        val result = driver.getDatabases
        result.onComplete{x => x.getOrElse(List.empty).foreach(println(_))}
      }
    }
  }
}

