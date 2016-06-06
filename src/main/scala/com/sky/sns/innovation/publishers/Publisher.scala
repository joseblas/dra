package com.sky.sns.innovation.publishers


import com.sky.sns.innovation.Showable
import com.typesafe.config.Config

import scala.concurrent.Future
import scala.util.Try

/**
 * The publisher is the component in charge
 * to write the data into an external system
 * which will make the data available for
 * consumption to other systems.
 *
 * If you want to integrate a new system to
 * the result of the draper App you have
 * to define your own publisher.
 *
 */
trait Publisher {

  /**
   * Is used to write a stream of (K, V) tuples where both K and V
   * are members of Showable Typeclass.
   * @param pair
   * @param meta
   * @tparam K
   * @tparam V
   * @return
   */
  def writeKeyValue[K:Showable, V:Showable](pair:(K, V), meta:Map[String, String] = Map.empty[String, String]):Future[Boolean]

  /**
   * Allows to publish a stream of element T of member of Showable
   * in a queue FIFO data structure.
   * @param value
   * @param meta
   * @tparam T
   * @return
   */
  def enqueue[T:Showable](value:T, meta:Map[String, String] = Map.empty[String, String]):Future[Boolean]

  /**
   * Allows to publish the stream of element T of member of Showable
   * in a stack LIFO data structure.
   * @param value
   * @param meta
   * @tparam T
   * @return
   */
  def stack[T:Showable](value:T, meta:Map[String, String] = Map.empty[String, String]):Future[Boolean]

  /**
   * ???
   * @return
   */
  //def connect():Future[Boolean]
  //def isConnected():Future[Boolean]
  //def close():Future[Boolean]

  def size2(): Int

}

object Publisher {
  /**
   * Publisher constructor using reflection.
   * All the publisher implementations have to accept
   * a {{settings:Config}} as input parameters.
   * @param info
   * @return instance of Publisher
   */
  def create(info:PublisherInfo):Try[Publisher] = {
    println("Create "+ info.clazz.toGenericString)
    println("Create "+ info.settings)
    Try(
      info.clazz.getConstructor(classOf[Config]).newInstance(info.settings)
    )
    }

}
