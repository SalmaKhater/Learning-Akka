import sbt._
import Keys._

object AkkaExcamples extends Build { 
    lazy val helloAkka = Project(id = "hello-akka", base = file("hello-akka"))
    lazy val playWithActors = Project(
    	id = "playing-with-actors", base = file("playing-with-actors"))
    lazy val actorPaths = Project(id = "actor-paths", base = file("actor-paths"))
    lazy val hotswap = Project(id = "hotswap-behavior", base = file("hotswap-behavior"))
    lazy val routing = Project(id = "routing", base = file("routing"))
    lazy val persistence = Project(id = "persistence", base = file("persistence"))
    lazy val persistenceQuery = Project(
    	id = "persistence-fsm-query", base = file("persistent-fsm&query"))
    lazy val remoting = Project(id = "akka-remoting", base = file("akka-remoting"))
    lazy val cluster = Project(id = "akka-cluster", base = file("akka-cluster"))
    lazy val testingActors = Project(id = "testing-actors", base= file("testing-actors"))
    lazy val testingParentChild = Project(
        id = "testing-parent-child", base= file("testing-parent-child"))
    lazy val testFSM = Project(id = "test-FSM", base = file("test-FSM"))
}

