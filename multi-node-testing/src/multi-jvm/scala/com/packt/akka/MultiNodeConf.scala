package com.packt.akka

import akka.remote.testkit.MultiNodeConfig
import com.typesafe.config.{ ConfigObject, ConfigFactory, Config }

object MultiNodeSampleConfig extends MultiNodeConfig {
  val node1 = role("node1")
  val node2 = role("node2")
}