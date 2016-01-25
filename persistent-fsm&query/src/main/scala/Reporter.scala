package com.packt.akka

import akka.stream.scaladsl.Source
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.persistence.query.{ PersistenceQuery, EventEnvelope}
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal


object Reporter extends App {

  val system = ActorSystem("persistent-query")

  implicit val mat = ActorMaterializer()(system)

  val queries = PersistenceQuery(system).readJournalFor[LeveldbReadJournal](
  LeveldbReadJournal.Identifier
  )

  val evts: Source[EventEnvelope, Unit] =
    queries.eventsByPersistenceId("account")

  evts.runForeach { evt => println(s"Event $evt")}

  Thread.sleep(1000)

  system.terminate()

}