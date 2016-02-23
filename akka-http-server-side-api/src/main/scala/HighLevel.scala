package com.packt.akka

import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

object HighLevel extends App {

  implicit val system = ActorSystem("my-system")

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher

 /*
    path(RESOURCE_PATH) {
      method[get|put|post ...] {
        complete {
          // return your response is case you accept this request.
        }
        reject {
          // return your response is case you reject this request.
        }
      }
    }
  */
  val route =
    path(""){
      get {
        complete("Hello Akka HTTP Server Side API - High Level")
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.shutdown()) // and shutdown when done

}