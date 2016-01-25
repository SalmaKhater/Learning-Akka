package com.packt.akka.loadBalancing

import com.packt.akka.commons._
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import akka.actor.{ Actor, ActorRef, ActorSystem, Props } 
import akka.cluster.Cluster
import akka.routing.FromConfig
import akka.actor.ReceiveTimeout
import scala.util.Random


class Frontend extends Actor {
  import context.dispatcher

  val backend = context.actorOf(FromConfig.props(), name = "backendRouter")

  context.system.scheduler.schedule(3.seconds, 3.seconds, self, 
    Add(Random.nextInt(100), Random.nextInt(100)))

  def receive = {
    case addOp: Add =>
      println("Frontend: I'll forward add operation to backend node to handle it.")
      backend forward addOp

  }

}


object Frontend {

  private var _frontend: ActorRef = _ 

  val upToN = 200

  def initiate() = {
    val config = ConfigFactory.parseString("akka.cluster.roles = [frontend]").
      withFallback(ConfigFactory.load("loadbalancer"))

    val system = ActorSystem("ClusterSystem", config)
    system.log.info("Frontend will start when 2 backend members in the cluster.")
    //#registerOnUp
    Cluster(system) registerOnMemberUp {
      _frontend = system.actorOf(Props[Frontend],
        name = "frontend")
    }
    //#registerOnUp

  }

  def getFrontend = _frontend
}