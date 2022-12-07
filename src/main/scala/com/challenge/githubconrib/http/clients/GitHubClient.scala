package com.challenge.githubconrib.http.clients

import com.challenge.githubconrib.domain.model.{Contributor, Organization, Repository}
import zhttp.http.Headers
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.json._
import zio._

/**
 * GitHub API HTTP Client
 */
trait GitHubClient {

  /**
   * Finds all Organizations
   *
   * @return effect with list of Organizations
   */
  def findAllOrganizations(): Task[List[Organization]]

  /**
   * Loads Organization by name
   *
   * @param name organization name
   * @return effect with Organization
   */
  def findOrganizationByName(name: String): Task[Organization]

  /**
   * Finds Repositories for Organization
   *
   * @param orgName organization name
   * @return effect with list of Repositories
   */
  def findRepositories(orgName: String): Task[List[Repository]]

  /**
   * Finds Contributors for Repository
   *
   * @param orgName organization name
   * @param repoName repository name
   * @return effect with list of Contributors
   */
  def findContributors(orgName: String, repoName: String): Task[List[Contributor]]
}

/**
 * Live implementation of GitHubClient
 */
final case class GitHubClientLive() extends GitHubClient {

  type ClientEnv = EventLoopGroup with ChannelFactory

  private val env = ChannelFactory.auto ++ EventLoopGroup.auto()

  private val baseUrl = "https://api.github.com"

  private val headers = Headers(
    "User-Agent" -> "GitHub-Contributors-App",
    "Accept"     -> "application/vnd.github.v3+json"
  )

  /**
   * Execute GET request to GitHub API
   *
   * @param url URL address
   * @param headers HTTP headers
   * @tparam T result type param
   * @return effect with result
   */
  private def getRequest[T: JsonDecoder](url: String, headers: Headers): ZIO[ClientEnv, Throwable, T] =
    for {
      res  <- Client.request(url = url, headers = headers)
      body <- res.bodyAsString
      data <- ZIO
        .fromEither(body.fromJson[T])
        .mapError(error => new Throwable(error))
    } yield data

  override def findAllOrganizations(): Task[List[Organization]] =
    getRequest[List[Organization]](s"$baseUrl/organizations", headers).provideLayer(env)

  override def findOrganizationByName(name: String): Task[Organization] =
    getRequest[Organization](s"$baseUrl/orgs/$name", headers).provideLayer(env)

  override def findRepositories(orgName: String): Task[List[Repository]] =
    getRequest[List[Repository]](s"$baseUrl/orgs/$orgName/repos", headers).provideLayer(env)

  override def findContributors(orgName: String, repoName: String): Task[List[Contributor]] =
    getRequest[List[Contributor]](s"$baseUrl/repos/$orgName/$repoName/contributors", headers)
      .provideLayer(env)
}

object GitHubClientLive {

  lazy val layer: ULayer[GitHubClient] =
    ZLayer.succeed(GitHubClientLive())
}
