package com.packt.akka

import akka.actor.{Props, ActorSystem}
import com.packt.akka.Worker.Work

object RouterApp extends App {

  val system = ActorSystem("router")

  val router = system.actorOf(Props[RouterPool])

  router ! Work()

  router ! Work()

  router ! Work()

  Thread.sleep(100)

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")
  system.actorOf(Props[Worker], "w4")
  system.actorOf(Props[Worker], "w5")

  val workers: List[String] = List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5")
  val routerGroup = system.actorOf(Props(classOf[RouterGroup], workers))

  routerGroup ! Work()

  routerGroup ! Work()

  Thread.sleep(100)

  system.terminate()
}