package com.challenge.githubconrib.domain

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

object model {

  // Organization model
  case class Organization(id: Int, login: String, url: String, description: Option[String], repos_url: String)

  object Organization {
    implicit val decoder: JsonDecoder[Organization] = DeriveJsonDecoder.gen
    implicit val encoder: JsonEncoder[Organization] = DeriveJsonEncoder.gen
  }

  // Repository model
  case class Repository(
      id: Int,
      name: String,
      full_name: String,
      description: Option[String],
      contributors_url: String
  )

  object Repository {
    implicit val decoder: JsonDecoder[Repository] = DeriveJsonDecoder.gen
    implicit val encoder: JsonEncoder[Repository] = DeriveJsonEncoder.gen
  }

  // Contributor model
  case class Contributor(id: Int, login: String, contributions: Int)

  object Contributor {
    implicit val decoder: JsonDecoder[Contributor] = DeriveJsonDecoder.gen
    implicit val encoder: JsonEncoder[Contributor] = DeriveJsonEncoder.gen
  }
}
