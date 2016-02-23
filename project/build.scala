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
    lazy val multiNodeTest = Project(id = "multiNodeTest", 
        base = file("multi-node-testing"))
    lazy val streams = Project(id = "streams", base = file("akka-streams"))
    lazy val reactiveTweets = Project(id = "reactiveTweets", 
        base = file("reactive-tweets"))
    lazy val streamsTests = Project(id = "stream-test", 
        base = file("stream-test"))
    lazy val graphFlow = Project(id = "graphFlow", base = file("graph-flows"))
    lazy val streamsIO = Project(id = "stream-io", base = file("stream-io"))
    lazy val httpClient = Project(id = "httpClient", 
        base = file("akka-http-client-side-api"))
    lazy val httpServer = Project(id = "httpServer", 
        base = file("akka-http-server-side-api"))
    lazy val restApi = Project(id = "restApi", 
        base = file("rest-api"))
    lazy val balancingWorkload = Project(id = "balancingWorkload", 
        base = file("balancing-workload"))
    lazy val orderTermination = Project(id = "orderedTermination", 
        base = file("ordered-termination"))
    lazy val schedulerMessages = Project(id = "schedulerMessages",
        base = file("scheduler-messages"))
    lazy val shutdown = Project(id = "shutdownPattern", 
        base = file("shutdown-pattern")) 
    lazy val throttlerMessages = Project(id = "throttlerMessages", 
        base = file("throttler-messages"))
}

