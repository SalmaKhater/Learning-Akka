package com.packt.akka

import akka.actor.{ ActorSystem, Props, Stash, FSM }

object UserStorageFSM {
  // FSM State
  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  // FSM Data
  sealed trait Data
  case object EmptyData extends Data


  trait DBOperation
  object DBOperation{
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object Disconnect
  case class Operation(op: DBOperation, user: User)

  case class User(username: String, email: String)
}


class UserStorageFSM extends FSM[UserStorageFSM.State, UserStorageFSM.Data] with Stash{
  import UserStorageFSM._

  // 1. define start with
  startWith(Disconnected, EmptyData)

  // 2. define states
  when(Disconnected){
    case Event(Connect, _) =>
      println("UserStorage Connected to DB")
      unstashAll()
      goto(Connected) using(EmptyData)
    case Event(_, _) =>
      stash()
      stay using(EmptyData)
  }

  when(Connected) {
    case Event(Disconnect, _) =>
      println("UserStorage disconnected from DB")
      goto(Disconnected) using EmptyData

    case Event(Operation(op, user), _) =>
      println(s"UserStorage receive ${op} operation to do in user: ${user}")
      stay using EmptyData
  }

  // 3. initialize
  initialize()

}


object FiniteStateMachine extends App {
  import UserStorageFSM._

  val system = ActorSystem("Hotswap-FSM")

  val userStorage = system.actorOf(Props[UserStorageFSM], "userStorage-fsm")

  userStorage ! Connect

  userStorage ! Operation(DBOperation.Create, User("Admin", "admin@packt.com"))

  userStorage ! Disconnect

  Thread.sleep(100)

  system.terminate()

}