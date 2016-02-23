package com.akka.packt

import akka.actor.{ActorSystem, Actor, Props, PoisonPill}
import scala.concurrent.duration._
import com.akka.pattern._
import java.util.Date

class Target extends Actor with ReaperWatched {

  def receive = {
    case msg =>
      println(s"[${new Date().toString}]I received a message: $msg")
  }
}

object ShutdownApp extends App{

  val system = ActorSystem("shutdown")

  val reaper = system.actorOf(Props[Reaper], Reaper.name)

  val target = system.actorOf(Props[Target], "target")

  target ! "Hello World"

  target ! PoisonPill

}