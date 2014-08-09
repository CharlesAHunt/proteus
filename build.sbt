name := "com/cornfluence/proteus"

version := "0.1.0"

scalaVersion := "2.11.2"

resolvers ++= Seq(
  "OSS"						at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= {
  Seq(
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
    "co.blocke"		% "scalajack_2.11"			% "2.0.2"
  )
}

