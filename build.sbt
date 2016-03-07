organization := "me.owlcode"

name := "scala-lirc"

version := "0.1.1"

description := "A simple Scala wrapper on top of LIRC (Linux Infrared Remote Control)"

crossScalaVersions := Seq("2.10.4", "2.11.7")

scalaVersion := crossScalaVersions.value.last

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

licenses ++= Seq("MIT" -> url(
  s"https://github.com/vburca/${name.value}/blob/${version.value}/LICENSE"))

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test")

bintrayPackageLabels := Seq("LIRC", "IoT", "RaspberryPi")
