import com.charlesahunt.proteus.config.ProteusConfig

package object proteus {

  val testConfig = ProteusConfig(
    host = "localhost",
    port = "",
    tls: Boolean,
    user: String,
    password: String,
    dataBaseName: String
  )

}
