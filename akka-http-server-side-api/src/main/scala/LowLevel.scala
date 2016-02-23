package com.packt.akka

import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._


object LowLevel extends App {

  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher
   
  val serverSource = Http().bind(interface = "localhost", port = 8888)
   
  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach{ connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWithSyncHandler(requestHandler)
    }).run()
   
  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity("Hello Akka HTTP Server Side API - Low Level!"))

    case _ =>
      HttpResponse(404, entity = "Unknown resource!")
  }

  println(s"Server online at http://localhost:8888/\nPress RETURN to stop...")
  Console.readLine()
 
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.shutdown()) // and shutdown when done

}