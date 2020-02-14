resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Classpaths.sbtPluginReleases,
  "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
)

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.6.1")
addSbtPlugin("org.scoverage"    % "sbt-scoverage"       % "1.6.1")
addSbtPlugin("com.codacy"       % "sbt-codacy-coverage" % "3.0.3")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"        % "2.3.1")
