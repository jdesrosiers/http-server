name := """http-server"""

version := "1.0"

organization := "jdesrosiers"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "io.javaslang" % "javaslang" % "2.0.6",
  "org.jparsec" % "jparsec" % "3.0",
  "org.apache.tika" % "tika-core" % "1.15",

  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "com.tngtech.java" % "junit-dataprovider" % "1.12.0" % "test"
)
