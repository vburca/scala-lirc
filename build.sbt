name := "scala-lirc"

version := "0.1.0"

description := "A simple Scala wrapper on top of LIRC (Linux Infrared Remote Control)"

crossScalaVersions := Seq("2.10.4", "2.11.7")

scalaVersion := crossScalaVersions.value.last

licenses ++= Seq("MIT" -> url(
  s"https://github.com/vburca/${name.value}/blob/${version.value}/LICENSE"))

libraryDependencies ++= Seq(
  // "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test")
