name := "Persistence"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  	"com.typesafe.akka"           %% "akka-actor"       % "2.4.0",
  	"com.typesafe.akka"           %% "akka-persistence" % "2.4.0",
  	"org.iq80.leveldb"            % "leveldb"           % "0.7",
  	"org.fusesource.leveldbjni"   % "leveldbjni-all"    % "1.8",
  	"com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.0",
  	"com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0")