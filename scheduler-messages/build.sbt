name := "throttler messages"
 
organization := "akka.pattern.throttle"
 
scalaVersion := "2.11.7"

sbtVersion := "0.13.5"
 
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
