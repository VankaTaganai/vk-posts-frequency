import sbt._

object Dependencies {
  val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"   % "2.8.0",
    "org.typelevel" %% "cats-effect" % "3.3.14",
  )

  val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-dsl" % "0.23.12",
    "org.http4s" %% "http4s-blaze-server" % "0.23.12",
    "org.http4s" %% "http4s-blaze-client" % "0.23.12",
    "org.http4s" %% "http4s-circe" % "0.23.12",
  )

  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-literal" % "0.13.0",
  )

  val derevo: Seq[ModuleID] = Seq(
    "org.manatki" %% "derevo-circe" % "0.11.6",
    "org.manatki" %% "derevo-core" % "0.11.6",
    "org.manatki" %% "derevo-pureconfig" % "0.11.6",
  )

  val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest"                     % "3.2.14" % Test,
    "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0"  % Test
  )
}
