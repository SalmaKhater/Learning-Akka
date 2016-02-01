package com.packt.akka

import org.scalatest.MustMatchers
import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestKit, TestActorRef, TestProbe, ImplicitSender }
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }

class ChildSpec extends TestKit(ActorSystem("test-system")) 
                  with ImplicitSender
                  with FlatSpecLike
                  with BeforeAndAfterAll 
                  with MustMatchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Child Actor" should "send pong message when receive ping message" in {
    val parent  = TestProbe()

    val child = system.actorOf(Props(new Child(parent.ref)))

    child ! "ping"

    parent.expectMsg("pong")
  }

}