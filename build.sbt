name := "cats"

version := "1.0"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.8.1",
  "org.apache.kafka" % "kafka-clients" % "0.10.0.1",
  "org.slf4j" % "slf4j-simple" % "1.7.21")
    