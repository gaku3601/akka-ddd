name := "akka-ddd"

version := "0.1"

scalaVersion := "2.13.3"

val AkkaVersion = "2.6.10"
val AkkaHttpVersion = "10.2.1"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.github.pureconfig" %% "pureconfig" % "0.14.0",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)