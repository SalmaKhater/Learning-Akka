package com.packt.akka

import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Identify, ActorIdentity }

class Watcher extends Actor {

	var counterRef: ActorRef = _

	val selection = context.actorSelection("/user/counter")

	selection ! Identify(None)

	def receive = {
		case ActorIdentity(_, Some(ref)) =>
			println(s"Actor Reference for counter is ${ref}")
		case ActorIdentity(_, None) =>
			println("Actor selection for actor doesn't live :( ")

	}
}