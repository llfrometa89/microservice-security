import sbt._

object Dependencies {
  object Versions {
    val catsVersion       = "2.1.0"
    val catsEffectVersion = "2.1.1"
    val scalaTestVersion  = "3.2.0-M2"
    val monocleVersion    = "2.0.1"
    val scalaCheckVersion = "1.14.3"
    val http4sVersion     = "0.21.1"
    val circeVersion      = "0.12.3"
    val logbackVersion    = "1.2.3"
    val awsSdkVersion     = "1.11.722"
    val pureConfigVersion = "0.12.2"
    val kindProjector     = "0.11.0"
    val betterMonadicFor  = "0.3.1"
    val catsMeowMtl       = "0.3.0-M1"
  }

  object Libraries {
    def circe(artifact: String): ModuleID = "io.circe" %% artifact % Versions.circeVersion

    lazy val cats                 = "org.typelevel" %% "cats-core" % Versions.catsVersion
    lazy val catsEffect           = "org.typelevel" %% "cats-effect" % Versions.catsEffectVersion
    lazy val monocleCore          = "com.github.julien-truffaut" %% "monocle-core" % Versions.monocleVersion
    lazy val monocleMacro         = "com.github.julien-truffaut" %% "monocle-macro" % Versions.monocleVersion
    lazy val catsMeowMtl          = "com.olegpy" %% "meow-mtl" % Versions.catsMeowMtl
    lazy val http4sBlazeServe     = "org.http4s" %% "http4s-blaze-server" % Versions.http4sVersion
    lazy val http4sBlazeClient    = "org.http4s" %% "http4s-blaze-client" % Versions.http4sVersion
    lazy val http4sCirce          = "org.http4s" %% "http4s-circe" % Versions.http4sVersion
    lazy val http4sDsl            = "org.http4s" %% "http4s-dsl" % Versions.http4sVersion
    lazy val circeGeneric         = circe("circe-generic")
    lazy val circeParser          = circe("circe-parser")
    lazy val logbackClassic       = "ch.qos.logback" % "logback-classic" % Versions.logbackVersion
    lazy val awsJavaSdk           = "com.amazonaws" % "aws-java-sdk" % Versions.awsSdkVersion
    lazy val awsJavaSdkCore       = "com.amazonaws" % "aws-java-sdk-core" % Versions.awsSdkVersion
    lazy val awsJavaSdkCognitoidp = "com.amazonaws" % "aws-java-sdk-cognitoidp" % Versions.awsSdkVersion
    lazy val pureConfig           = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfigVersion
    lazy val scalaTest            = "org.scalatest" %% "scalatest" % Versions.scalaTestVersion
    lazy val scalaCheck           = "org.scalacheck" %% "scalacheck" % Versions.scalaCheckVersion
    lazy val betterMonadicFor     = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
    lazy val kindProjector        = "org.typelevel" %% "kind-projector" % Versions.kindProjector
  }
}
