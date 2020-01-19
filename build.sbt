import scala.io.Source

name := "proteus"

organization := "com.charlesahunt"

description := "Scala driver for ArangoDB"

version := Source.fromFile("./.version").getLines().toList.head

scalaVersion := "2.13.1"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

scalacOptions ++= Seq("-feature")

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

sonatypeProfileName := "com.charlesahunt"

useGpg := false

publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
   else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra in Global := {
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
      </developers>
  }

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

parallelExecution in Test := false

resolvers ++= Seq(
  "OSS"  at "http://oss.sonatype.org/content/repositories/releases"
)

shellPrompt := { state => scala.Console.YELLOW + "[" + scala.Console.CYAN + Project.extract(state).currentProject.id + scala.Console.YELLOW + "]" + scala.Console.RED + " $ " + scala.Console.RESET }

libraryDependencies ++= {
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.scalaj" %% "scalaj-http" % "2.4.2",
    "com.github.pureconfig" %% "pureconfig" % "0.12.2",
    "org.scalatest" %% "scalatest" % "3.1.0" % "test"
  )
}

val circeVersion = "0.13.0-RC1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)