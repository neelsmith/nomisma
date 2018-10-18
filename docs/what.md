---
title: What can you do with this library?
layout: page
---

## Work with data in simple tabular formats

Some of the `nomisma.org` data sets are already converted to tabular formats:  if you just want to work with `csv` files, using databases, GIS, or data visualization applicatoins,
look in the `tables` directory of <https://github.com/neelsmith/nomisma>.

Example:  see a [Tableau visualization of Greek coin hoards](https://public.tableau.com/profile/neel.smith#!/vizhome/IGCHgeolocatemints/Hoardsandmints).



## Write your own code to analyze `nomisma.org` data

You can parse data sets freely available in RDF XML from <http://www.nomisma.org/datasets > into an object model.


`nomisma` is compiled for both the JVM and ScalaJS using scala versions 2.11 and 2.12, so you can use this library in web apps written in ScalaJS or on any JVM platfrom from your android phone to distributed systems like Apache Spark.

Binaries for all platforms are available from jcenter.  If you are using `sbt` to manage your project, include `Resolver.jcenterRepo` in your list of resolvers:

    resolvers += Resolver.jcenterRepo

and add this to your library dependencies:

    "edu.holycross.shot.cite" %%% "nomisma" % VERSION


For maven, ivy or gradle equivalents, refer to <https://bintray.com/neelsmith/maven/nomisma>.
