import com.charlesahunt.proteus.config.ProteusConfig

package object proteus {

  val testConfig = ProteusConfig(
      user = "testUser",
      password = "pass",
      database = "testDB"
  )

}
