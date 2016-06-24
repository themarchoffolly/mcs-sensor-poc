name := """mcs-sensor-poc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
    "org.webjars" % "bootstrap" % "3.3.6",
    "org.webjars" % "jquery" % "2.2.4",
    "com.google.code.gson" % "gson" % "2.4",
    "io.onetapbeyond" % "opencpu-r-executor" % "1.1"
)
