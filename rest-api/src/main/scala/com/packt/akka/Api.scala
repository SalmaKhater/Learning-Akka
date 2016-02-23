package com.packt.akka

import spray.json._
import DefaultJsonProtocol._
import akka.actor.ActorSystem
import scala.concurrent.Future
import akka.stream.{ ActorMaterializer, Materializer }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.packt.akka.models._
import akka.http.scaladsl.model.StatusCodes._
import com.packt.akka.db.TweetManager
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.concurrent.ExecutionContext

trait RestApi {
  import TweetProtocol._
  import TweetEntity._
  import TweetEntityProtocol.EntityFormat

  implicit val system: ActorSystem

  implicit val materializer: Materializer

  implicit val ec: ExecutionContext

  val route =
    pathPrefix("tweets"){
      (post & entity(as[Tweet])) { tweet =>
        complete {
          TweetManager.save(tweet) map { r =>
            Created -> Map("id" -> r.id).toJson
          }
        }
      } ~
        (get & path(Segment)) { id =>
          complete {
            TweetManager.findById(id) map { t =>
              OK -> t
            }
          }
        } ~
        (delete & path(Segment)) { id =>
          complete {
            TweetManager.deleteById(id) map { _ =>
              NoContent
            }
          }
        } ~
        (get) {
          complete {
            TweetManager.find map { ts =>
              OK -> ts.map(_.as[TweetEntity])
            }
          }
        }
    }

}
object Api extends App with RestApi {

  override implicit val system = ActorSystem("rest-api")

  override implicit val materializer = ActorMaterializer()

  override implicit val ec = system.dispatcher
 
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
 
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine()
 
  bindingFuture
    .flatMap(_.unbind()) 
    .onComplete(_ => system.shutdown()) 

}