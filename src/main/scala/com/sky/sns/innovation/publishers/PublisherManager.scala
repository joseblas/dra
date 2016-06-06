package com.sky.sns.innovation.publishers

import com.sky.sns.innovation.DConfig

import scala.util.{Failure, Success}

/**
 * This is the publisher runtime manager.
 * It is instantiated on each Spark Executor and will
 * make sure that publisher are created only once and not
 * on each RDD/DStream processing.
 */
object PublisherManager extends Serializable {

  @transient private var publishersMap = Map.empty[String, Publisher]

  def getPublisher(name:String)(implicit config:DConfig):Option[Publisher] = publishersMap.get(name).orElse {

    config.publisherRegistry.get(name).flatMap { pi =>
      Publisher.create(pi) match {
        case Success(pub) =>
          publishersMap += name -> pub
          Some(pub)
        case Failure(_) =>
          None
      }

    }

  }
}
