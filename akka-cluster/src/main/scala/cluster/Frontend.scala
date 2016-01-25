package com.packt.akka.cluster

import akka.cluster._
import scala.util.Random
import com.packt.akka.commons._
import com.typesafe.config.ConfigFactory
import akka.actor.{ Actor, ActorRef, ActorSystem, Props, Terminated }


class Frontend extends Actor {

  var backends = IndexedSeq.empty[ActorRef]

  def receive = {
    case Add if backends.isEmpty =>
      println("Service unavailable, cluster doesn't have backend node.")
    case addOp: Add =>
      println("Frontend: I'll forward add operation to backend node to handle it.")
      backends(Random.nextInt(backends.size)) forward addOp
    case BackendRegistration if !(backends.contains(sender())) =>
      backends = backends :+ sender()
      context watch(sender())
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)

  }

}


object Frontend {

  private var _frontend: ActorRef = _ 

  def initiate() = {
   val config = ConfigFactory.load().getConfig("Frontend")

    val system = ActorSystem("ClusterSystem", config)

    _frontend = system.actorOf(Props[Frontend], name = "frontend")
  }

  def getFrontend = _frontend
}