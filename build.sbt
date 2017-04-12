name := """http-server"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  // Uncomment to use Akka
  //"com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "io.javaslang" % "javaslang" % "2.0.6",

  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.hamcrest" % "hamcrest-all" % "1.3" % "test",
  "com.tngtech.java" % "junit-dataprovider" % "1.12.0" % "test"
)
