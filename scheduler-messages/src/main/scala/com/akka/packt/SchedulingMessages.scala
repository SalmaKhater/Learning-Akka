package com.akka.packt

import akka.actor.{ActorSystem, Actor, Props}
import scala.concurrent.duration._

object SchedulingMessages extends App{

  val system = ActorSystem("Scheduling-Messages")

//  val scheduler = system.actorOf(Props[ScheduleInConstructor], "schedule-in-constructor")


  val scheduler = system.actorOf(Props[ScheduleInReceive], "schedule-in-receive")
  Thread.sleep(5000)
  system.shutdown()

}