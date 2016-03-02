organization := "me.owlcode"

name := "scala-lirc"

version := "0.1.0"

description := "A simple Scala wrapper on top of LIRC (Linux Infrared Remote Control)"

crossScalaVersions := Seq("2.10.4", "2.11.7")

scalaVersion := crossScalaVersions.value.last

licenses ++= Seq("MIT" -> url(
  s"https://github.com/vburca/${name.value}/blob/${version.value}/LICENSE"))

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test")

bintrayPackageLabels := Seq("LIRC", "IoT", "RaspberryPi")
