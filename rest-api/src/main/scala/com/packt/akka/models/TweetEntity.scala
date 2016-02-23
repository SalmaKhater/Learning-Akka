package com.packt.akka.models

import spray.json._
import scala.util._
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}

case class TweetEntity(id: BSONObjectID = BSONObjectID.generate,
                      author: String,
                      body: String)

object TweetEntity {
  implicit def toTweetEntity(tweet: Tweet) = 
    TweetEntity(author = tweet.author, body = tweet.body)


  implicit object TweetEntityBSONReader extends BSONDocumentReader[TweetEntity] {
    
    def read(doc: BSONDocument): TweetEntity = 
      TweetEntity(
        id = doc.getAs[BSONObjectID]("_id").get,
        author = doc.getAs[String]("author").get,
        body = doc.getAs[String]("body").get
      )
  }
  
  implicit object TweetEntityBSONWriter extends BSONDocumentWriter[TweetEntity] {
    def write(tweetEntity: TweetEntity): BSONDocument =
      BSONDocument(
        "_id" -> tweetEntity.id,
        "author" -> tweetEntity.author,
        "body" -> tweetEntity.body
      )
  }
}

object TweetEntityProtocol extends DefaultJsonProtocol {

  implicit object BSONObjectIdProtocol extends RootJsonFormat[BSONObjectID] {
    override def write(obj: BSONObjectID): JsValue = JsString(obj.stringify)
    override def read(json: JsValue): BSONObjectID = json match {
      case JsString(id) => BSONObjectID.parse(id) match {
        case Success(validId) => validId
        case _ => deserializationError("Invalid BSON Object Id")
      }
      case _ => deserializationError("BSON Object Id expected")
    }
  }

  implicit val EntityFormat = jsonFormat3(TweetEntity.apply)
}