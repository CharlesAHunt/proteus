name := "proteus"

organization := "com.cornfluence"

description := "Scala driver for ArangoDB"

version := "0.2.1"

scalaVersion := "2.11.2"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
   else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
   <url>http://www.cornfluence.com</url>
      <scm>
         <url>git@github.com:CharlesAHunt/proteus.git</url>
         <connection>scm:git@github.com:CharlesAHunt/proteus.git</connection>
      </scm>
      <developers>
         <developer>
            <id>CharlesAHunt</id>
            <name>Charles A Hunt</name>
            <url>http://www.cornfluence.com</url>
         </developer>
      </developers>)

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

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

