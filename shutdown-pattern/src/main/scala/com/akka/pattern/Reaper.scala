package com.akka.pattern

import akka.actor.{Actor, ActorRef, Terminated}
import scala.collection.mutable.ArrayBuffer

object Reaper {
  val name = "reaper"
  // Used by others to register an Actor for watching
  case class WatchMe(ref: ActorRef)
}

class Reaper extends Actor {
  import Reaper._

  // Keep track of what we're watching
  val watched = ArrayBuffer.empty[ActorRef]

  // Derivations need to implement this method.  It's the
  // hook that's called when everything's dead
  def allSoulsReaped() = {
    println("SYSTEM SHUTDOWN START")
    context.system.shutdown()
    println("SYSTEM SHUTDOWN END")
  }

  // Watch and check for termination
  final def receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      watched += ref
    case Terminated(ref) =>
      watched -= ref
      if (watched.isEmpty) allSoulsReaped()
  }
}

trait ReaperWatched { this: Actor => 
  override def preStart() {
    println("IN PRESTART")
    context.actorFor("/user/" + Reaper.name) ! Reaper.WatchMe(self)
  }
}