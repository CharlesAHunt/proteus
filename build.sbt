import scala.io.Source
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import xerial.sbt.Sonatype._

name := "proteus"

organization := "com.charlesahunt"

description := "Scala driver for ArangoDB"

version := Source.fromFile("./.version").getLines().toList.head

scalaVersion := "2.13.13"

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

sonatypeProfileName := "com.charlesahunt"

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

sonatypeProjectHosting := Some(GitHubHosting("CharlesAHunt", "proteus", "charlesalberthunt@gmail.com"))

homepage := Some(url("http://www.cornfluence.com"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/CharlesAHunt/proteus"),
    "scm:git@github.com:CharlesAHunt/proteus.git"
  )
)

developers := List(
  Developer(id="CharlesAHunt", name="Charles A Hunt", email="charlesalberthunt@gmail.com", url=url("http://www.cornfluence.com"))
)

parallelExecution in Test := false

resolvers ++= Seq(
  "OSS"  at "https://oss.sonatype.org/content/repositories/releases"
)

shellPrompt := { state => scala.Console.YELLOW + "[" + scala.Console.CYAN + Project.extract(state).currentProject.id + scala.Console.YELLOW + "]" + scala.Console.RED + " $ " + scala.Console.RESET }

val circeVersion = "0.14.6"

libraryDependencies ++= {
  Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.typelevel" %% "cats-core" % "2.10.0",
    "org.typelevel" %% "cats-effect" % "2.0.0",
    "ch.qos.logback" % "logback-classic" % "1.5.6",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "org.scalaj" %% "scalaj-http" % "2.4.2",
    "com.github.pureconfig" %% "pureconfig" % "0.17.6",
    "org.scalatest" %% "scalatest" % "3.2.18" % "test"
  )
}

promptTheme := com.scalapenos.sbt.prompt.PromptThemes.ScalapenosTheme

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros", // Allow macro definition (besides implementation and application)
  "-language:higherKinds", // Allow higher-kinded types
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Ybackend-parallelism", "8", // Enable paralellisation — change to desired number!
  "-Ycache-plugin-class-loader:last-modified", // Enables caching of classloaders for compiler plugins
  "-Ycache-macro-class-loader:last-modified", // and macro definitions. This can lead to performance improvements.
)