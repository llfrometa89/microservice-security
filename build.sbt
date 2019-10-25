import Dependencies._

name := "microservice-security"

organization := "io.github.llfrometa89"

version := "0.1.0"

scalaVersion := "2.12.8"

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-Ypartial-unification",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xlog-reflective-calls",
  "-Ywarn-inaccessible",
  "-Ypatmat-exhaust-depth",
  "20",
  "-Ydelambdafy:method",
  "-Xmax-classfile-name",
  "100",
  "-opt:l:inline",
  "-opt-inline-from:**"
)

libraryDependencies ++= Seq(
  compilerPlugin(Libraries.kindProjector)
)

libraryDependencies ++= Seq(
  Libraries.cats,
  Libraries.catsEffect,
  Libraries.monocleCore,
  Libraries.monocleMacro,
  Libraries.http4sBlazeServe,
  Libraries.http4sBlazeClient,
  Libraries.http4sCirce,
  Libraries.http4sDsl,
  Libraries.circeGeneric,
  Libraries.logbackClassic,
  Libraries.awsJavaSdk,
  Libraries.awsJavaSdkCore,
  Libraries.awsJavaSdkCognitoidp,
  Libraries.pureConfig,
  Libraries.scalaTest  % Test,
  Libraries.scalaCheck % Test
)

scalacOptions ++= commonScalacOptions
