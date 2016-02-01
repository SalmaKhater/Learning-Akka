package com.packt.akka

import org.scalatest.MustMatchers
import akka.actor.{ ActorSystem, Props, ActorRefFactory }
import akka.testkit.{ TestKit, TestProbe, ImplicitSender }
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }

class ParentSpec extends TestKit(ActorSystem("test-system"))
                  with ImplicitSender
                  with FlatSpecLike
                  with BeforeAndAfterAll
                  with MustMatchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Parent" should "send ping message to child when receive ping it message" in {
    val child = TestProbe()

    val childMaker = (_: ActorRefFactory) => child.ref

    val parent = system.actorOf(Props(new Parent(childMaker)))

    parent ! "ping"

    child.expectMsg("ping")
  }

}