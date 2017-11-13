name := """test-bugtracker"""
organization := "avgolubev"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
    guice
//  , jdbc
  , "org.postgresql" % "postgresql" % "42.1.4"
  , "com.typesafe.play" %% "play-slick" % "3.0.0"
  , "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
  , "org.apache.lucene" % "lucene-core" % "7.1.0"
  , "org.apache.lucene" % "lucene-analyzers-common" % "7.1.0"
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "avgolubev.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "avgolubev.binders._"
