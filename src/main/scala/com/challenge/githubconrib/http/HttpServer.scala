package com.challenge.githubconrib.http

import com.challenge.githubconrib.http.routes.OrganizationRoutes
import zhttp.service.Server
import zio.{URLayer, ZIO, ZLayer}

/**
 * HTTP Server implementation
 *
 * @param organizationRoutes OrganizationRoutes definition
 */
final case class HttpServer(organizationRoutes: OrganizationRoutes) {

  val start: ZIO[Any, Throwable, Nothing] =
    Server.start(8080, organizationRoutes.routes)
}

object HttpServer {

  lazy val layer: URLayer[OrganizationRoutes, HttpServer] =
    ZLayer.fromFunction(HttpServer.apply _)
}
