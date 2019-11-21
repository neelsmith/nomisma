# `nomisma` library for numismatic data


`nomisma` is a cross-platform library for working with freely available numismatic data from <http://nomisma.org>.

## Current version: **0.2.0**.

Status: early development. [Release notes](releases.md)

Current models implemented in Scala are based on content of the *Inventory of Greek Coin Hoards* (*IGCH*).

The `quarry` directory has some kludgey scripts to work with data from the *Online Coins of the Roman Empire* (*OCRE*).

Before a 1.0 release, the *OCRE* material should be integrated with classes based on *IGCH* to implement a consistent set of objects for issues, mints and hoards.


## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)

## Using, building, testing

`nomisma` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12.  Binaries for all platforms are available from jcenter.

If you are using `sbt`, include `Resolver.jcenterRepo` in your list of resolvers

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot.cite" %%% "nomisma" % VERSION


For maven, ivy or gradle equivalents, refer to <https://bintray.com/neelsmith/maven/nomisma>.



`nomisma` is built using [sbt](http://www.scala-sbt.org/). To build from source and test, use normal `sbt` commands (`compile`, `test` ...)

## Documentation

Is beginning to appear at <https://neelsmith.github.io/nomisma/>.


## Data sets

The directory `ids` is a copy of this directory in the `nomisma.org` data repository: <https://github.com/nomisma/data/tree/master/id>.  It was last refreshed on Nov 18, 2017.
