ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "vk-posts-frequency",
    libraryDependencies ++= Seq(
      Dependencies.cats,
      Dependencies.http4s,
      Dependencies.circe,
      Dependencies.derevo,
    ).flatten,
    scalacOptions ++= Seq(
      "-Ymacro-annotations"
    ),
    javaOptions += "-Dconfig.file=src/test/resources/application.conf"
  )
