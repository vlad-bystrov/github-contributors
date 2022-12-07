import sbt._

object Dependencies {
  // versions
  lazy val zioVersion     = "2.0.0-RC6"
  lazy val zioHttpVersion = "2.0.0-RC9"
  lazy val zioJsonVersion = "0.3.0-RC8"
  lazy val logbackVersion = "1.2.11"

  // libraries
  val zio     = "dev.zio"       %% "zio"             % zioVersion
  val zioHttp = "io.d11"        %% "zhttp"           % zioHttpVersion
  val zioJson = "dev.zio"       %% "zio-json"        % zioJsonVersion
  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime

  // projects
  val backendDeps = Seq(zio, zioHttp, zioJson, logback)
}
