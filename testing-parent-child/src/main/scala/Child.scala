package com.packt.akka

import akka.actor.{ActorRef, Actor}

class Child(parent: ActorRef) extends Actor {
  def receive = {
    case "ping" => parent ! "pong"
  }
}