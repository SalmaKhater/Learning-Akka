package com.akka.packt

import akka.actor.{ActorSystem, Actor, Props}
import scala.concurrent.duration._
import com.akka.pattern.throttle.TimerBasedThrottler
import java.util.Date


class Target extends Actor {

  def receive = {
    case msg =>
      println(s"[${new Date().toString}}] I receive msg: $msg")
  }
}


object ThottlerApp extends App{
  import com.akka.pattern.throttle.Throttler._

  val system = ActorSystem("Thottler-Messages")

  val target = system.actorOf(Props[Target], "target")

  val throttler = system.actorOf(Props(classOf[TimerBasedThrottler], 3 msgsPer 1.second))

  throttler ! SetTarget(Some(target))

  throttler ! "1"
  throttler ! "2"
  throttler ! "3"
  throttler ! "4"
  throttler ! "5"

  Thread.sleep(5000)
  system.shutdown()

}