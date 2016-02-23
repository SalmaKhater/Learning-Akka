package com.packt.akka

import spray.json.DefaultJsonProtocol

case class IpInfo(ip: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(IpInfo.apply)
}