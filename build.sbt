name := "nomisma"

//"Utilities for working with numismatic data from nomisma.org"

// airframe logger only works with 2.12, 2.13
//crossScalaVersions in ThisBuild := Seq("2.11.8", "2.12.4")
//crossScalaVersions in ThisBuild := Seq("2.12.4","2.13.1")
crossScalaVersions in ThisBuild := Seq("2.12.4")
scalaVersion := (crossScalaVersions in ThisBuild).value.last


lazy val root = project.in(file(".")).
    aggregate(crossedJVM, crossedJS).
    settings(
      publish := {},
      publishLocal := {}

    )

lazy val crossed = crossProject.in(file(".")).
    settings(
      name := "nomisma",
      organization := "edu.holycross.shot",
      version := "2.0.1",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),

      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6",

        "org.wvlet.airframe" %%% "airframe-log" % "19.8.10",

        "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
        "edu.holycross.shot.cite" %%% "xcite" % "4.1.1",
        "edu.holycross.shot" %%% "ohco2" % "10.16.0",

        "edu.holycross.shot" %%% "histoutils" % "2.2.0"
      )
    ).
    jvmSettings(

    ).
    jsSettings(
      skip in packageJSDependencies := false,
      scalaJSUseMainModuleInitializer in Compile := true

    )

lazy val crossedJVM = crossed.jvm
lazy val crossedJS = crossed.js//.enablePlugins(ScalaJSPlugin)
