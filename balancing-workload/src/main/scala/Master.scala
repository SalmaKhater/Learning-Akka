package com.packt.akka

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}

object MasterWorkerProtocol {
  // Messages from Workers
  case class WorkerCreated(worker: ActorRef)
  case class WorkerRequestsWork(worker: ActorRef)
  case class WorkIsDone(worker: ActorRef)

  // Messages to Workers
  case class WorkToBeDone(work: Any)
  case object WorkIsReady
  case object NoWorkToBeDone
}

class Master extends Actor with ActorLogging {
  import MasterWorkerProtocol._
  import scala.collection.mutable.{Map, Queue}

  // Holds known workers and what they may be working on
  val workers = Map.empty[ActorRef, Option[Tuple2[ActorRef, Any]]]
  // Holds the incoming list of work to be done as well
  // as the memory of who asked for it
  val workQ = Queue.empty[Tuple2[ActorRef, Any]]

  // Notifies workers that there's work available, provided they're
  // not already working on something
  def notifyWorkers(): Unit = {
    if (!workQ.isEmpty) {
      workers.foreach { 
        case (worker, m) if (m.isEmpty) => worker ! WorkIsReady
        case _ =>
      }
    }
  }

  def receive = {
    // Worker is alive. Add him to the list, watch him for
    // death, and let him know if there's work to be done
    case WorkerCreated(worker) =>
      log.info("Worker created: {}", worker)
      context.watch(worker)
      workers += (worker -> None)
      notifyWorkers()

    // A worker wants more work.  If we know about him, he's not
    // currently doing anything, and we've got something to do,
    // give it to him.
    case WorkerRequestsWork(worker) =>
      log.info("Worker requests work: {}", worker)
      if (workers.contains(worker)) {
        if (workQ.isEmpty)
          worker ! NoWorkToBeDone
        else if (workers(worker) == None) {
          val (workSender, work) = workQ.dequeue()
          workers += (worker -> Some(workSender -> work))
          // Use the special form of 'tell' that lets us supply
          // the sender
          worker.tell(WorkToBeDone(work), workSender)
        }
      }

    // Worker has completed its work and we can clear it out
    case WorkIsDone(worker) =>
      if (!workers.contains(worker))
        log.error("Blurgh! {} said it's done work but we didn't know about him", worker)
      else
        workers += (worker -> None)

    // A worker died.  If he was doing anything then we need
    // to give it to someone else so we just add it back to the
    // master and let things progress as usual
    case Terminated(worker) =>
      if (workers.contains(worker) && workers(worker) != None) {
        log.error("Blurgh! {} died while processing {}", worker, workers(worker))
        // Send the work that it was doing back to ourselves for processing
        val (workSender, work) = workers(worker).get
        self.tell(work, workSender)
      }
      workers -= worker
      context.unwatch(worker)

    // Anything other than our own protocol is "work to be done"
    case work =>
      log.info("Queueing {}", work)
      workQ.enqueue(sender -> work)
      notifyWorkers()
  }
}
