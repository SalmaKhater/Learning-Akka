package com.packt.akka

import akka.stream.actor.ActorPublisher


class StatusPublisher extends ActorPublisher[Tweet] {

  val sub = context.system.eventStream.subscribe(self, classOf[Tweet])

  override def receive: Receive = {
    case s: Tweet => {
      if (isActive && totalDemand > 0) onNext(s)
    }
    case _ =>
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }

}