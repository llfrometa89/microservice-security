import Dependencies._

name := "microservice-security"

organization := "io.github.llfrometa89"

version := "0.1.0"

scalaVersion := "2.13.1"

lazy val commonScalacOptions = Seq(
  "-feature",
  "-language:higherKinds",
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-Wunused:imports,patvars,locals",
  "-Wnumeric-widen",
  "-Xlint:-unused"
)

libraryDependencies ++= Seq(
//  compilerPlugin(Libraries.kindProjector),
  compilerPlugin(Libraries.betterMonadicFor)
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  Libraries.cats,
  Libraries.catsEffect,
  Libraries.catsMeowMtl,
  Libraries.monocleCore,
  Libraries.monocleMacro,
  Libraries.http4sBlazeServe,
  Libraries.http4sBlazeClient,
  Libraries.http4sCirce,
  Libraries.http4sDsl,
  Libraries.circeGeneric,
  Libraries.circeParser,
  Libraries.logbackClassic,
  Libraries.awsJavaSdk,
  Libraries.awsJavaSdkCore,
  Libraries.awsJavaSdkCognitoidp,
  Libraries.pureConfig,
  Libraries.scalaTest  % Test,
  Libraries.scalaCheck % Test
)

scalacOptions ++= commonScalacOptions

enablePlugins(JavaAppPackaging)
