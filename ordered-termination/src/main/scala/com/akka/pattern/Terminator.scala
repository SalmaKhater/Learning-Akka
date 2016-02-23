package com.akka.pattern

import akka.actor.{Actor, ActorRef, Terminated, Props}
import akka.pattern.{gracefulStop, pipe}
import scala.concurrent.Future
import akka.util.Timeout
import scala.concurrent.duration._

object Terminator {
  // Protocol between us and the master
  case class GetChildren(forActor: ActorRef)
  case class Children(kids: Iterable[ActorRef])
}

class Terminator(childProps: Props, numOfChildren: Int) extends Actor {
  import Terminator._
  import context._

  implicit val stopTimeout = 5.minutes
  case object AllDead

  override def preStart() {
    (1 to numOfChildren).foreach { i =>
      context.actorOf(childProps, s"worker-${i}")
    }
  }

  // Derivations implement
  def order(kids: Iterable[ActorRef]): Iterable[ActorRef] = {
    kids.toSeq.reverse
  }

  // Kills kids in the order given by the list
  def killKids(kids: List[ActorRef]): Future[Any] = {
    kids match {
      case kid :: Nil =>
        gracefulStop(kid, stopTimeout).flatMap { _ =>
          Future { AllDead }
        }
      case Nil =>
        Future { AllDead }
      case kid :: rest =>
        gracefulStop(kid, stopTimeout).flatMap { _ =>
          killKids(rest)
        }
    }
  }

  // Initially we're waiting for the request for kids
  def waiting: Receive = {
    case GetChildren(forActor) =>
      watch(forActor)
      forActor ! Children(children)
      become(childrenGiven(forActor))
  }

  // Once we've done it, we want to lock our aspect
  // to the guy we gave them to
  def childrenGiven(to: ActorRef): Receive = {
    case GetChildren(forActor) if sender == to =>
      forActor ! Children(children)
    case Terminated(`to`) =>
      killKids(order(children).toList) pipeTo self
    case AllDead =>
      stop(self)
  }

  // Start waiting
  def receive = waiting
}