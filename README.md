# TypedGraph

A library to describe **Typed Condensed Oriented Directed Acyclic Planar Multigraphs**.

For more information, read the [white paper](https://github.com/NikinAI/TypedGraph/tree/main/docs).

## How to install

For convenience, and for the time being, we are using GitHub Packages, which is really not straightforward to use at the moment. 

Hopefully, it will get better soon.

### Step 1: Install your credentials.

1. Create a [personal access token](https://github.com/settings/tokens/new). 
    * You will need only one permission: `read:packages`.
2. Save the token in `~/.gitconfig`
    * under `github.token`
    * It should look like:
    ```
    [github]
       token = my_token
    ```
   Alternatively, you can store the token under `GITHUB_TOKEN` in the environment.

### Step 2: Set up your project

1. Add resolver to `build.sbt`:
   * ```sbt
      resolvers += "GitHub Package Registry (NikinAI/TypedGraph)" at "https://maven.pkg.github.com/NikinAI/TypedGraph"
     ```
2. Add credentials:
   * ```sbt
      credentials +=
        Credentials(
          realm = "GitHub Package Registry",
          host = "maven.pkg.github.com",
          userName = "_",
          passwd = {
            import scala.util.Try
            import scala.sys.process._
            sys.env.getOrElse("GITHUB_TOKEN", Try(s"git config github.token".!!).map(_.trim).get)
          },
        ),
     ```
3. Add the library:
   * ```sbt
      libraryDependencies += "ai.nikin" %% "typed-graph" % "<VERSION>",
     ``` 
   * For version, look at [latest package](https://github.com/NikinAI/TypedGraph/packages/)

