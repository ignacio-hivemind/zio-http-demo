lazy val root = project
  .in(file("."))
  .settings(
    name         := "zio-http",
    organization := "com.hivemind",
    description  := "Sample project using Scala 3 and ZIO Http",
    version      := "1.0.0",
    scalaVersion := "3.5.2",
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "clean; fmt; all compile Test/compile IntegrationTest/compile; test")

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

val ZIOVersion       = "2.1.10"
val ScalaTestVersion = "3.2.19"
val zioHttpVersion   = "3.0.1"
val circeVersion     = "0.14.10"

libraryDependencies ++= Seq(
  // ZIO
  "dev.zio"       %% "zio"              % ZIOVersion,
  "dev.zio"       %% "zio-streams"      % ZIOVersion,
  "dev.zio"       %% "zio-test"         % ZIOVersion       % Test,
  "dev.zio"       %% "zio-test-sbt"     % ZIOVersion       % Test,
  "dev.zio"       %% "zio-http"         % zioHttpVersion,
  "dev.zio"       %% "zio-http-testkit" % zioHttpVersion   % Test,
  "io.circe"      %% "circe-core"       % circeVersion,
  "io.circe"      %% "circe-parser"     % circeVersion,
  "io.circe"      %% "circe-generic"    % circeVersion,
  "org.scalactic" %% "scalactic"        % ScalaTestVersion,
  "org.scalatest" %% "scalatest"        % ScalaTestVersion % Test,
)

resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"

scalacOptions ++= Seq(
  "-deprecation",     // emit warning and location for usages of deprecated APIs
  "-explain",         // explain errors in more detail
  "-explain-types",   // explain type errors in more detail
  "-feature",         // emit warning and location for usages of features that should be imported explicitly
  "-indent",          // allow significant indentation.
  "-new-syntax",      // require `then` and `do` in control expressions.
  "-print-lines",     // show source code line numbers.
  "-unchecked",       // enable additional warnings where generated code depends on assumptions
  "-Xkind-projector", // allow `*` as wildcard to be compatible with kind projector
  "-Werror",          // fail the compilation if there are any warnings
  "-Xmigration",      // warn about constructs whose behavior may have changed since version
  "-explain-cyclic",
)
