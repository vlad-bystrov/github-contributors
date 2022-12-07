## GitHub Contributors Ranking Application

### Overview

GitHub portal is centered around organizations and repositories. Each organization has many repositories and each 
repository has many contributors. Your goal is to create an endpoint that given the name of the organization will 
return a list of contributors sorted by the number of contributions.

### Endpoints

REST API consist of the following endpoints:

* `/orgs` - find all organizations
* `/orgs/{name}` - get organization by name
* `/orgs/{name}/repos` - find repositories for organization
* `/orgs/{name}/repos/{repo}` - find contributors for repository
* `/orgs/{name}/contributors` - rank contributors for organization

### How to run

To run application use sbt:

```shell
sbt compile run
```

The application server will be running at port 8080. Then REST endpoints can be called via curl:

For example:

```shell
curl http://localhost:8080/orgs/ORG/repos
```
