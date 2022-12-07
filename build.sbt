import Dependencies._

ThisBuild / organization := "com.challenge.githubconrib"
ThisBuild / version      := "1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-language:postfixOps"
)

lazy val root = (project in file("."))
  .settings(
    name := "github-contributors",
    libraryDependencies ++= backendDeps
  )
