# `nomisma` library for numismatic data

`nomisma` is a cross-platform library for working with freely available numismatic data from <http://nomisma.org>.  It defines classes that can read the RDF data formats used by `nomisma.org`, and build objects modelling numismatic concepts like issues, mints, and hoards.  

Follow work on this project at <https://neelsmith.github.io/nomisma/>.


## Current version: **0.7.0**.

Status: in active development. [Release notes](releases.md)

Current models implemented in Scala are based on content of the *Inventory of Greek Coin Hoards* (*IGCH*) and on the *Online Coins of the Roman Empire* (*OCRE*).

## A note on working with OCRE data

OCRE documents more than 50,000 issues of Roman imperial coins.  The RDF file openly available from nomisma.org is more than 178 megabytes, too big to include in this git repository.  The tabular data files in `jvm/src/test/resources/cex` are built using the `nomisma` library's `OcreRdf` object from a copy of the RDF file downloaded in November 2017.  The composite file representing each OCRE issue as a single record with 15 fields is less than 19 megabytes.  The library's `Ocre` object easily can parse that on a modest consumer-level laptop.

If you wish to verify or update the data in the tabular data files, you can download a copy of the OCRE data from <http://nomisma.org/datasets>, and parse the resulting RDF file with the `OcreRdf` object. This requires a lot memory. To start an sbt console session, for example, you could use:

    SBT_OPTS="-Xms512M -Xmx4096M -XX:MaxMetaspaceSize=1024M" sbt


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



## Data sets

The directory `ids` is a copy of this directory in the `nomisma.org` data repository: <https://github.com/nomisma/data/tree/master/id>.  It was last refreshed on Nov 18, 2017.
