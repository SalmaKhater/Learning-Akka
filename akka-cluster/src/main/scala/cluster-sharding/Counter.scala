package com.packt.akka.cluster.sharding

import akka.persistence._
import scala.concurrent.duration._
import akka.cluster.sharding.ShardRegion
import akka.actor.{ Actor, ActorRef, Props, ActorLogging }
 
class Counter extends PersistentActor with ActorLogging {
  import Counter._
 
  context.setReceiveTimeout(120.seconds)
 
  override def persistenceId: String = self.path.parent.name + "-" + self.path.name
 
  var count = 0
 
  def updateState(event: CounterChanged): Unit =
    count += event.delta
 
  override def receiveRecover: Receive = {
    case evt: CounterChanged ⇒ updateState(evt)
  }
 
  override def receiveCommand: Receive = {
    case Increment => 
      log.info(s"Counter with path: ${self} recevied Increment Command")
      persist(CounterChanged(+1))(updateState)
    case Decrement => 
      log.info(s"Counter with path: ${self} recevied Decrement Command")
      persist(CounterChanged(-1))(updateState)
    case Get => 
      log.info(s"Counter with path: ${self} recevied Get Command")
      log.info(s"Count = ${count}")
      sender() ! count
    case Stop => 
      context.stop(self)
  }
}

object Counter {

  trait Command
  case object Increment extends Command
  case object Decrement extends Command
  case object Get extends Command
  case object Stop extends Command

  trait Event
  case class CounterChanged(delta: Int) extends Event

  // Sharding Name
  val shardName: String = "Counter"

  // outside world if he want to send message to sharding should use this message
  case class CounterMessage(id: Long, cmd: Command)

  // id extrator
  val idExtractor: ShardRegion.ExtractEntityId = {
    case CounterMessage(id, msg) => (id.toString, msg)
  }
 
  // shard resolver
  val shardResolver: ShardRegion.ExtractShardId = {
    case CounterMessage(id, msg) => (id % 12).toString
  }

  def props() = Props[Counter]

}