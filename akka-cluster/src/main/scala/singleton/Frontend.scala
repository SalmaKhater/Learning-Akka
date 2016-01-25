package com.packt.akka.cluster.singleton

import scala.concurrent.duration._
import akka.cluster.singleton.{ ClusterSingletonProxy, ClusterSingletonProxySettings}
import akka.actor.{ Actor, Props, ActorLogging, ActorRef }

class Frontend extends Actor with ActorLogging {
  import Frontend._
  import context.dispatcher

  val masterProxy = context.actorOf(ClusterSingletonProxy.props(
    singletonManagerPath = "/user/master",
    settings = ClusterSingletonProxySettings(context.system).withRole(None)
  ), name = "masterProxy")

  context.system.scheduler.schedule(0.second, 3.second, self, Tick)

  def receive = {
    case Tick =>
      masterProxy ! Master.Work(self, "add")
  }

}

object Frontend {
  case object Tick

  def props = Props(new Frontend())
}