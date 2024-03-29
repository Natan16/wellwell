name := """wellwellb"""
organization := "com.saragatasoftware"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += ws
libraryDependencies += "org.playframework" %% "play-json" % "3.0.2"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.saragatasoftware.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.saragatasoftware.binders._"
