package com.challenge.githubconrib

import com.challenge.githubconrib.http.HttpServer
import com.challenge.githubconrib.http.clients.GitHubClientLive
import com.challenge.githubconrib.http.routes.OrganizationRoutes
import com.challenge.githubconrib.services.OrganizationsLive
import zio._

object Main extends ZIOAppDefault {

  // application entry point
  override def run: ZIO[Any, Throwable, Unit] =
    ZIO
      .serviceWithZIO[HttpServer](_.start)
      .provide(
        HttpServer.layer,
        OrganizationRoutes.layer,
        OrganizationsLive.layer,
        GitHubClientLive.layer
      )
}
