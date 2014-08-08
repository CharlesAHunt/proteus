name := "com/cornfluence/proteus"

version := "0.1.0"

scalaVersion := "2.11.2"

libraryDependencies ++= {
  Seq(
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )
}

