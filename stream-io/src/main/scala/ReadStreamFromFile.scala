package com.packt.akka

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.io.Framing
import akka.stream.scaladsl._
import akka.stream.stage.{ Context, StatefulStage, SyncDirective }
import akka.util.ByteString

import scala.annotation.tailrec

import akka.stream.io.Implicits._

object ReadStream extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  // read lines from a log file
  val logFile = new File("src/main/resources/log.txt")

  val source = Source.synchronousFile(logFile)

  // parse chunks of bytes into lines
  val flow = Framing.delimiter(ByteString(System.lineSeparator()),
    maximumFrameLength = 512,
    allowTruncation = true).map(_.utf8String)

  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink).andThen {
     case _ =>
      actorSystem.shutdown()
      actorSystem.awaitTermination()
  }
}
