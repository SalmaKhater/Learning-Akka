import sbt._
import Keys._

object AkkaExcamples extends Build { 
    lazy val helloAkka = Project(id = "hello-akka", base = file("hello-akka"))
    lazy val playWithActors = Project(
    	id = "playing-with-actors", base = file("playing-with-actors"))
}
