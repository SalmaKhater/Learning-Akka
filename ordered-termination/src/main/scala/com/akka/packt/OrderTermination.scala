package com.akka.packt

import akka.actor.{ActorSystem, Actor, Props, PoisonPill}
import scala.concurrent.duration._
import com.akka.pattern._
import java.util.Date

object OrderTermination extends App{

  val system = ActorSystem("order-termination")

  val terminator = system.actorOf(Props(new Terminator(Props[Worker], 5)))

  val master = system.actorOf(Props(new Master(terminator)))

  master ! "hello world"

  master ! PoisonPill

  Thread.sleep(5000)
  system.shutdown()

}