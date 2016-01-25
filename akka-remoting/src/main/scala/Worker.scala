package com.packt.akka

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }

class Worker extends Actor {
  import Worker._

  def receive = {
    case msg: Work =>
      println(s"I received Work Message and My ActorRef: ${self}")
  }
}

object Worker {
  case class Work(message: String) 
}