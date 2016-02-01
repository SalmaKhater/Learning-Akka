package com.packt.akka

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.util.{ Failure, Success }

import akka.stream.io.Implicits._

object WriteStream extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  // Source
  val source = Source(1 to 10000).filter(isPrime)

  // Sink
  val sink = Sink.synchronousFile(new File("target/prime.txt"))

  // file output sink
  val fileSink = Flow[Int]
    .map(i => ByteString(i.toString))
    .toMat(sink)((_, bytesWritten) => bytesWritten)

  // console output sink
  val consoleSink = Sink.foreach[Int](println)

  // send primes to both file sink and console sink using graph API
  val g = FlowGraph.closed(fileSink, consoleSink)((file, _) => file) { implicit builder =>
    (file, console) =>
      import FlowGraph.Implicits._

    val broadCast = builder.add(Broadcast[Int](2))

    source ~> broadCast ~> file
              broadCast ~> console

  }.run()

  // ensure the output file is closed and the system shutdown upon completion
  g.onComplete {
    case Success(_) =>
      actorSystem.shutdown()
    case Failure(e) =>
      println(s"Failure: ${e.getMessage}")
      actorSystem.shutdown()
  }

  def isPrime(n: Int): Boolean = {
    if (n <= 1) false
    else if (n == 2) true
    else !(2 to (n - 1)).exists(x => n % x == 0)
  }
}
