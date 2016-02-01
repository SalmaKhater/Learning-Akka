//package com.packt.akka
//
//import akka.actor.{ ActorSystem, Props }
//import akka.stream.ActorMaterializer
//import akka.stream.scaladsl.{Flow, Sink, Source}
//
//import scala.collection.immutable
//import twitter4j.Status
//
//object StreamTweets extends App {
//  implicit val actorSystem = ActorSystem()
//  import actorSystem.dispatcher
//  implicit val flowMaterializer = ActorMaterializer()
//
//  val streamClient = new TwitterStreamClient(actorSystem)
//  streamClient.init
//
//  val source = Source.actorPublisher[Tweet](Props[StatusPublisher])
//
//  val normalize = Flow[Tweet].filter { t =>
//    t.hashtags.contains(Hashtag("#Akka"))
//  }
//
//  val sink = Sink.foreach[Tweet] { customer =>
//    println(customer)
//  }
//
//  source.via(normalize).runWith(sink).andThen {
//    case _ =>
//      actorSystem.shutdown()
//      actorSystem.awaitTermination()
//  }
//}
