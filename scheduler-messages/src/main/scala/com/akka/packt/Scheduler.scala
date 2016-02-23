package com.akka.packt

import akka.actor.{Actor}
import scala.concurrent.duration._
import java.util.Date

class ScheduleInConstructor extends Actor {
  import context._

  val tick =
    context.system.scheduler.schedule(500 millis, 1.second, self, "tick")

  override def postStop() = tick.cancel()
 
  def receive = {
    case "tick" =>
      println(s"Cool! I got tick message at ${new Date().toGMTString}")
  }
}


class ScheduleInReceive extends Actor {
  import context._
 
  override def preStart() =
    context.system.scheduler.scheduleOnce(500 millis, self, "tick")
 
  // override postRestart so we don't call preStart and schedule a new message
  override def postRestart(reason: Throwable) = {}
 
  def receive = {
    case "tick" =>
      println(s"Cool! I got tick message at ${new Date().toGMTString}")
      // send another periodic tick after the specified delay
      context.system.scheduler.scheduleOnce(1000 millis, self, "tick")

  }
}

