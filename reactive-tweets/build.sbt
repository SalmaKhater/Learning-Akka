name := "Reactive Tweets"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0",
  "org.twitter4j" % "twitter4j-stream" % "4.0.3"
)
