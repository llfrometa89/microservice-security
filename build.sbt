name := "microservice-security"

organization := "io.github.llfrometa89"

version := "0.1.0"

scalaVersion := "2.13.1"

lazy val V = new {
  val catsVersion       = "2.0.0"
  val catsEffectVersion = "2.0.0"
  val scalaTestVersion  = "3.2.0-M1"
  val monocleVersion    = "2.0.0"
  val scalaCheckVersion = "1.14.2"
  val http4sVersion     = "0.21.0-M5"
  val circeVersion      = "0.12.2"
  val logbackVersion    = "1.2.3"
}

libraryDependencies ++= Seq(
  "org.typelevel"              %% "cats-core"           % V.catsVersion,
  "org.typelevel"              %% "cats-effect"         % V.catsEffectVersion,
  "com.github.julien-truffaut" %% "monocle-core"        % V.monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro"       % V.monocleVersion,
  "org.http4s"                 %% "http4s-blaze-server" % V.http4sVersion,
  "org.http4s"                 %% "http4s-blaze-client" % V.http4sVersion,
  "org.http4s"                 %% "http4s-circe"        % V.http4sVersion,
  "org.http4s"                 %% "http4s-dsl"          % V.http4sVersion,
  "io.circe"                   %% "circe-generic"       % V.circeVersion,
  "ch.qos.logback"             % "logback-classic"      % V.logbackVersion,
  "org.scalatest"              %% "scalatest"           % V.scalaTestVersion % Test,
  "org.scalacheck"             %% "scalacheck"          % V.scalaCheckVersion % Test
)
