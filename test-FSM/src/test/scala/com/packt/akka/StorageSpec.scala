package com.packt.akka

import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestKit, TestFSMRef, TestProbe, ImplicitSender }
import org.scalatest.matchers.MustMatchers
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }

class UserStorageSpec extends TestKit(ActorSystem("test-system")) 
                      with ImplicitSender
                      with FlatSpecLike
                      with BeforeAndAfterAll 
                      with MustMatchers {
  import UserStorageFSM._

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "User Storage" should "Start with disconnected state and empty data" in {
    val storage = TestFSMRef(new UserStorageFSM())

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)

  }

  it should "be connected state if it receive a connect message" in {
    val storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage.stateName must equal(Connected)
    storage.stateData must equal(EmptyData)
  }

  it should "be still in disconnected state if it receive any other messages" in {
    val storage = TestFSMRef(new UserStorageFSM())

    storage ! DBOperation.Create

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)
  }

  it should "be switch to disconnected when it receive a disconnect message on Connected state" in {
    val storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage ! Disconnect

    storage.stateName must equal(Disconnected)
    storage.stateData must equal(EmptyData)
  }


  it should "be still on connected state if it receive any DB operations" in {
    val storage = TestFSMRef(new UserStorageFSM())

    storage ! Connect

    storage ! DBOperation.Create

    storage.stateData must equal(EmptyData)
    storage.stateName must equal(Connected)
  }

}