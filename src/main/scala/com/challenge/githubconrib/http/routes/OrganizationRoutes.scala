package com.challenge.githubconrib.http.routes

import com.challenge.githubconrib.services.Organizations
import zhttp.http._
import zio.{UIO, URLayer, ZIO, ZLayer}
import zio.json._

/**
 * Defines all Organization related REST API routes.
 *
 * @param organizations Organization service
 */
final case class OrganizationRoutes(organizations: Organizations) {

  private def notFound(): UIO[Response] =
    ZIO.succeed(Response.status(Status.NotFound))

  // define HTTP API routes
  val routes: HttpApp[Any, Throwable] = Http.collectZIO[Request] {

    // find contributors for repository
    case Method.GET -> !! / "orgs" / name / "repos" / repo =>
      organizations.findAllContributors(name, repo).map { repo =>
        Response.json(repo.toJson)
      } catchAll { _ =>
        notFound()
      }

    // find repositories for organization
    case Method.GET -> !! / "orgs" / name / "repos" =>
      organizations.findAllRepositories(name).map { repo =>
        Response.json(repo.toJson)
      } catchAll { _ =>
        notFound()
      }

    // rank contributors for organization
    case Method.GET -> !! / "orgs" / name / "contributors" =>
      organizations.rankContributors(name).map { contrib =>
        Response.json(contrib.toJson)
      } catchAll { _ =>
        notFound()
      }

    // get organization by name
    case Method.GET -> !! / "orgs" / name =>
      organizations.findByName(name).map { org =>
        Response.json(org.toJson)
      } catchAll { _ =>
        notFound()
      }

    // find all organizations
    case Method.GET -> !! / "orgs" =>
      organizations.findAll().map { orgs =>
        Response.json(orgs.toJson)
      } catchAll { _ =>
        notFound()
      }

    case _ => notFound()
  }
}

object OrganizationRoutes {

  lazy val layer: URLayer[Organizations, OrganizationRoutes] =
    ZLayer.fromFunction(OrganizationRoutes.apply _)
}
