name := "Utilities for working with numismatic data"

//crossScalaVersions := Seq("2.10.6","2.11.8", "2.12.1")
crossScalaVersions := Seq("2.11.8", "2.12.1")


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
      version := "0.1.0",
      licenses += ("GPL-3.0",url("https://opensource.org/licenses/gpl-3.0.html")),

      libraryDependencies ++= Seq(
        "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
        "org.scalatest" %%% "scalatest" % "3.0.1" % "test",
        "org.scala-lang.modules" %% "scala-xml" % "1.0.6"/*,
        "com.esri.geometry" % "esri-geometry-api" % "1.2.1"*/
      )
    ).
    jvmSettings(

    ).
    jsSettings(
      skip in packageJSDependencies := false,
      persistLauncher in Compile := true,
      persistLauncher in Test := false
    )

lazy val crossedJVM = crossed.jvm
lazy val crossedJS = crossed.js.enablePlugins(ScalaJSPlugin)
