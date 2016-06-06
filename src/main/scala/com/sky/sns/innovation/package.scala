package com.sky.sns


import com.sky.sns.innovation.publishers.PublisherManager
import org.apache.spark.streaming.Duration
import org.apache.spark.streaming.dstream.DStream
import play.api.libs.json.{JsValue, Json}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.FiniteDuration

package object innovation {

  /**
   * Wraps a {{DStream[(K, V)]}} where K and V are members of Showable
   * on a type which will allow us to call the method publish.
   * @param stream
   * @param ev1
   * @param ev2
   * @tparam K
   * @tparam V
   */
  implicit class PublishKVOp[K:Showable, V:Showable](@transient stream:DStream[(K, V)]) extends Serializable {
    def publish(publisherName:String, meta:Map[String, String] = Map.empty[String, String])(implicit config:DConfig):Unit = stream.foreachRDD { rdd =>
      rdd.foreachPartition {
          _.foreach { pair =>

//            PublisherManager.getPublisher(publisherName){
//              case Some(pub) =>
//            }

            PublisherManager.getPublisher(publisherName).map(_.size2())


            println("p "+ pair)
            PublisherManager.getPublisher(publisherName).map(_.writeKeyValue(pair, meta))

        }
      }
    }
  }

  /**
   * Typeclass definition for Showable.
   * Everything that has a readable representation of its
   * structure is a showable.
   *
   * @tparam S type S member of Showable. Note that Showable has a method show(s)
   *           with S in a contravariant position.
   */
  trait Showable[-S] extends Serializable {
    def show(s:S):String
  }

  /**
   * Wraps type S members of Showable into a class with a user friendly method show().
   * @param s
   * @param ev1
   * @tparam S
   */
  implicit class ShowableOp[S:Showable](s:S) {
    def show():String = implicitly[Showable[S]].show(s)
  }

  /**
   * Defines all AnyVal as showable.
   */
  implicit val anyValShowable = new Showable[AnyVal] {
    def show(value:AnyVal) = value.toString
  }

  /**
   * Makes strings showable.
   */
  implicit val strShowable = new Showable[String] {
    def show(value:String) = value
  }

  /**
   * Defines json as showable.
   */
  implicit val jsShowable = new Showable[JsValue] {
    def show(jsValue:JsValue) = Json.stringify(jsValue)
  }

  /**
   * A pair is a showable if both elements are showable too.
   * @tparam K
   * @tparam V
   * @return
   */
  implicit def pairShowable[K:Showable, V:Showable] = new Showable[(K, V)] {
    def show(kv:(K, V)) = kv.toString
  }

  object converters {
    /**
     * Convert scala FiniteDuration into Spark Duration.
     * @param time
     * @return
     */
    implicit def toSparkDuration(time:FiniteDuration):Duration = Duration(time.toMillis)
  }
}
