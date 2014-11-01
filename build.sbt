name := "proteus"

organization := "com.cornfluence"

description := "Scala driver for ArangoDB"

version := "0.1.2"

scalaVersion := "2.11.2"

parallelExecution in Test := false

resolvers ++= Seq(
  "OSS"  at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= {
  Seq(
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "co.blocke"	  %% "scalajack" % "2.0.2"
  )
}

