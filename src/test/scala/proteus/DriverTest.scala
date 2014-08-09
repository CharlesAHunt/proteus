package proteus

import co.blocke.scalajack.ScalaJack
import com.cornfluence.proteus.{Result, Driver}
import org.scalatest.FunSpec
import org.scalatest.Matchers._

class DriverTest extends FunSpec {

  describe("==============\n| Driver Test |\n==============") {
    describe("Get Databases") {
      it("should properly retrieve all databases in Arrango") {

        val driver = new Driver(databaseName = "test")
        val result = driver.getDatabases()
println(result)
        ScalaJack.read[Result](result)
      }
    }
  }
}

