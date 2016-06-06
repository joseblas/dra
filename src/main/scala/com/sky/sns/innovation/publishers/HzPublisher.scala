package com.sky.sns.innovation.publishers

import java.util.concurrent.TimeUnit

import com.hazelcast.client.HazelcastClient
import com.sky.sns.innovation.Showable
import com.typesafe.config.Config


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.hazelcast.client.config.ClientConfig
import scala.collection.JavaConverters._
import com.sky.sns.innovation._


class HzPublisher(settings:Config) extends Publisher {

  println("*******HzPublisher starting... ")
  val hzConfig = new ClientConfig()
  println("hzConfig "+ hzConfig)
  val servers = settings.getStringList("servers").asScala
  println("Servers "+ servers)
  for(server <- servers) hzConfig.getNetworkConfig.addAddress(server)
  hzConfig.getNetworkConfig.setSmartRouting(true)


  val hzConnection = HazelcastClient.newHazelcastClient(hzConfig)
  println("Connected "+ hzConnection)
  val mapName = settings.getString("map")
  val hzMap = hzConnection.getMap[String, String](settings.getString("map"))
  hzMap.clear() //to remove

  def size2(): Int = {

      val si = hzConnection.getMap(mapName).size().toInt
      println(s"Size $si")
      si

  }


  override def writeKeyValue[K:Showable, V:Showable](pair:(K, V), meta:Map[String, String]) = Future {
    val (k,v) = pair
    println("Key "+ k)
    println("Key "+ hzMap.getName)

    meta.get("ttl") match {
      case Some(ttl) => hzMap.put(k.show(), v.show(), ttl.toLong, TimeUnit.MILLISECONDS)
      case None => hzMap.put(k.show(), v.show())
    }
    true
  }

  override def enqueue[T:Showable](value:T, meta:Map[String, String]) = Future {
    ???
  }

  override def stack[T:Showable](value:T, meta:Map[String, String]) = Future {
    ???
  }

}
