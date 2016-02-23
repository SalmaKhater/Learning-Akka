package com.packt.akka

import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable


object RequestLevel extends App {
  import JsonProtocol._

  implicit val system = ActorSystem("Request-Level")

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher

  val responseFuture: Future[HttpResponse] =
    Http().singleRequest(HttpRequest(uri = "https://api.ipify.org?format=json"))

  responseFuture map { res =>
    res.status match {
      case OK =>
        Unmarshal(res.entity).to[IpInfo].map { info =>
          println(s"The information for my ip is: $info")
          shutdown()
        }
      case _ =>
        Unmarshal(res.entity).to[String].map { body =>
          println(s"The response status is ${res.status} and response body is ${body}")
          shutdown()
        }
    }
  }

  def shutdown() = {
    Http().shutdownAllConnectionPools().onComplete{ _ =>
      system.shutdown()
      system.awaitTermination()
    }

  }

}