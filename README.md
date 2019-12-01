
# `nomisma` library for numismatic data


`nomisma` is a cross-platform library for working with freely available numismatic data from <http://nomisma.org/datasets>.  It defines classes that can read the RDF data formats used by `nomisma.org`, and build objects modeling numismatic concepts like issues, mints, and hoards.  

Follow blog posts on this project at <http://neelsmith.info/tag-coins/>.



## Current version: **1.4.2**.

Status: in active development. [Release notes](releases.md)

Current models implemented in Scala are based on content of the *Inventory of Greek Coin Hoards* (*IGCH*) and on the *Online Coins of the Roman Empire* (*OCRE*).

## Data files


- `rdf` directory:  mirrors select files in RDF format available from <http://nomisma.org/datasets>.   It was last refreshed on Nov 18, 2017. The directory `ids` replicates data in the `nomisma.org` data repository <https://github.com/nomisma/data/tree/master/id>.
- `cex` directory: translation of RDF data from <http://nomisma.org/datasets> into delimited-text files.  `ocre.cex` was build from RDF source downloaded on November 28, 2019.


### A note on OCRE data

The file `cex/ocre.cex` formats data for more than 50,000 issues of Roman imperial coins in OCRE in a single delimited-text table, built by parsing <http://numismatics.org/ocre/nomisma.rdf>. While the resulting `.cex` file is just over 19 Mb and can be easily loaded by the library's `Ocre` object on a modest consumer-level laptop, the RDF source was more than 150 Mb. If you wish to verify or update the data, you can parse the RDF source with the `OcreRdf` object, but will need to ensure that your scala environment has enough memory for an in-memory parse. To start an sbt console session, for example, you could use:

    SBT_OPTS="-Xms512M -Xmx4096M -XX:MaxMetaspaceSize=1024M" sbt

## Documentation

- The blog posts at <http://neelsmith.info/tag-coins/> are accompanied by a github repository at <https://github.com/neelsmith/nomisma-jupyter>.  Work with the notebook repository on `mybinder.org`: [![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/neelsmith/nomisma-jupyter/master)
- (out of date) documentation: <https://neelsmith.github.io/nomisma/>


## License

[GPL 3.0](https://opensource.org/licenses/gpl-3.0.html)

## Using, building, testing

`nomisma` is compiled for both the JVM and ScalaJS using Scala version 2.12.  Binaries for all platforms are available from jcenter.

If you are using `sbt`, include `Resolver.jcenterRepo` in your list of resolvers

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot.cite" %%% "nomisma" % VERSION


For maven, ivy or gradle equivalents, refer to <https://bintray.com/neelsmith/maven/nomisma>.

`nomisma` is built using [sbt](http://www.scala-sbt.org/). To build from source and test, use normal `sbt` commands (`compile`, `test` ...)
