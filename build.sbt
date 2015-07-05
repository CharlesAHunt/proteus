import scala.io.Source

name := "proteus"

organization := "com.cornfluence"

description := "Scala driver for ArangoDB"

version := Source.fromFile("./.version").getLines().toList.head

scalaVersion := "2.11.4"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

scalacOptions ++= Seq("-feature")

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

shellPrompt := { state => scala.Console.YELLOW + "[" + scala.Console.CYAN + Project.extract(state).currentProject.id + scala.Console.YELLOW + "]" + scala.Console.RED + " $ " + scala.Console.RESET }

libraryDependencies ++= {
  Seq(
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
     "co.blocke" %% "scalajack" % "4.0"
  )
}

