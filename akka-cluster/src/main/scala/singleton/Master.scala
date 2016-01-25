package com.packt.akka.cluster.singleton

import akka.persistence._
import akka.actor.{ Actor, Props, ActorLogging, ActorRef }

object Master {

  trait Command
  case class RegisterWorker(worker: ActorRef) extends Command
  case class RequestWork(requester: ActorRef) extends Command
  case class Work(requester: ActorRef, op: String) extends Command

  trait Event
  case class AddWorker(worker: ActorRef) extends Event
  case class AddWork(work: Work) extends Event
  case class UpdateWorks(works: List[Work]) extends Event

  case class State(workers: Set[ActorRef], works: List[Work])

  case object NoWork
}

class Master extends PersistentActor with ActorLogging {
  import Master._

  var workers: Set[ActorRef] = Set.empty
  var works: List[Work] = List.empty

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name

  def updateState(evt: Event): Unit = evt match {
    case AddWorker(w) => 
      workers = workers + w

    case AddWork(w) => 
      works = works :+ w

    case UpdateWorks(ws) =>
      works = ws 

  }

  val receiveRecover: Receive = {
    case evt: Event =>
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) => 
      workers = snapshot.workers
      works = snapshot.works
  }

  val receiveCommand: Receive = {
    case RegisterWorker(worker) =>
      persist(AddWorker(worker)) { evt =>
        updateState(evt)
      }

    case RequestWork if works.isEmpty =>
      sender() ! NoWork

    case RequestWork(requester) if workers.contains(requester) && ! works.isEmpty =>
      sender() ! works.head
      persist(UpdateWorks(works.tail)) { evt =>
        updateState(evt)
      }

    case w: Work =>
      persist(AddWork(w)) { evt =>
        updateState(evt)
      }

  }
}
