package com.challenge.githubconrib.services

import com.challenge.githubconrib.domain.model.{Contributor, Organization, Repository}
import com.challenge.githubconrib.http.clients.GitHubClient
import zio._

/**
 * Organization service API definition.
 */
trait Organizations {

  /**
   * Finds all Organizations
   *
   * @return effect with list of Organizations
   */
  def findAll(): Task[List[Organization]]

  /**
   * Loads Organization by name
   *
   * @param name organization name
   * @return effect with Organization
   */
  def findByName(name: String): Task[Organization]

  /**
   * Finds Repositories for Organization
   *
   * @param orgName organization name
   * @return effect with list of Repositories
   */
  def findAllRepositories(orgName: String): Task[List[Repository]]

  /**
   * Finds Contributors for Repository
   *
   * @param orgName organization name
   * @param repoName repository name
   * @return effect with list of Contributors
   */
  def findAllContributors(orgName: String, repoName: String): Task[List[Contributor]]

  /**
   * Ranks Contributors by all Repositories of an Organization
   *
   * @param orgName organization name
   * @return effect with list of Contributors sorted by contributions
   */
  def rankContributors(orgName: String): Task[List[Contributor]]
}

/**
 * Organization service live implementation.
 *
 * @param gitHubClient GitHubClient service
 */
final case class OrganizationsLive(gitHubClient: GitHubClient) extends Organizations {

  override def findAll(): Task[List[Organization]] =
    gitHubClient.findAllOrganizations()

  override def findByName(name: String): Task[Organization] =
    gitHubClient.findOrganizationByName(name)

  override def findAllRepositories(orgName: String): Task[List[Repository]] =
    gitHubClient.findRepositories(orgName)

  override def findAllContributors(orgName: String, repoName: String): Task[List[Contributor]] =
    gitHubClient.findContributors(orgName, repoName)

  /**
   * Ranks Contributors by all Repositories of an Organization
   *
   * @param orgName organization name
   * @return effect with list of Contributors sorted by contributions
   */
  override def rankContributors(orgName: String): Task[List[Contributor]] = {
    val result = for {
      repos <- findAllRepositories(orgName)
      contrib <- ZIO.collectAll {
        repos.map(r => findAllContributors(orgName, r.name))
      }
      ranked = reduceValues(contrib.flatten)
    } yield ranked

    result
  }

  private def reduceValues(list: List[Contributor]): List[Contributor] = list
    .map(c => (c, c.contributions))
    .groupBy(t => t._1)
    .view
    .mapValues(list => list.map(_._2).sum)
    .toList
    .map(t => t._1.copy(contributions = t._2))
}

object OrganizationsLive {

  lazy val layer: URLayer[GitHubClient, Organizations] =
    ZLayer.fromFunction(OrganizationsLive.apply _)
}
