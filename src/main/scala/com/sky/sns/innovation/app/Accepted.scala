package com.sky.sns.innovation.app

import com.sky.sns.innovation.{DConfig, NCStreamingApp, NCStreamingContext}
//import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.streaming._
import scala.concurrent.duration._
/**
  * Created by dev on 22/01/16.
  */
class Accepted extends NCStreamingApp {

  /**
    * set the interval of execution
    */
  override val interval = 5 seconds




  /**
    * Application entry point
    *
    * @param context current context of execution
    * @param config  current configuration
    */
  override def start(implicit context: NCStreamingContext, config: DConfig) = {

    val test = "{" +
      "\"currency\":\"PLN\"," +
      "\"currPrecision\":2," +
      "\"trips\":" +
        "[{\"origin\":\"RZE\",\"destination\":\"LTN\",\"dates\":" +
          "  {\"dateOut\":\"2016-05-24T00:00:00.000\",\"flights\":[{\"flightNumber\":\"FR 3473\",\"time\":[\"2016-05-24T21:00:00.000\",\"2016-05-24T22:35:00.000\"],\"timeUTC\":[\"2016-05-24T19:00:00.000Z\",\"2016-05-24T21:35:00.000Z\"],\"duration\":\"02:35\",\"faresLeft\":4,\"flightKey\":\"FR~3473~ ~~RZE~05/24/2016 21:00~LTN~05/24/2016 22:35~\",\"infantsLeft\":10,\"regularFare\":{\"fareKey\":\"0~E~~EZ8LOW~BND8~~2~X\",\"fareClass\":\"E\",\"fares\":[{\"type\":\"ADT\",\"amount\":439.0000,\"count\":1,\"hasDiscount\":false,\"publishedFare\":439.0000}]},\"businessFare\":{\"fareKey\":\"0~E~~ELDISBUS~YYZ1~~8~X\",\"fareClass\":\"E\",\"fares\":[{\"type\":\"ADT\",\"amount\":550.0000000,\"count\":1,\"hasDiscount\":true,\"publishedFare\":639.0000}]}}]}]}" +
      " ],\"serverTimeUTC\":\"2016-05-11T15:59:09.782Z\"}"


    val f = context.stream("flights", "1.0")
    f.print()
     val f2 = f.map(i =>
      new FactWithId
        (
//          (i.payload \ "currency" \ "dates" \ "flights" \ "flightKey").toString(),
          (((i.payload \\ "trips")(0) \\ "flights")(0) \\ "flightNumber" ).head.as[String].replaceAll(" ", "")+"-"+
            ((i.payload \\ "trips")(0) \\ "dateOut" ).head.as[String].substring(0,10),
          i.facttype, // facttype
          i.version, // Version
          i.time,
          i.source,
          i.acquisitiontime,
          i.payload
          )
      )
//    .saveToCassandra("ra", "messages")
    //          .joinWithCassandraTable("ufo","orders",joinColumns = SomeColumns("id"))

    f2.print()
    f2.saveToCassandra("ra", "messages")

    //

    // import com.datastax.spark.connector.streaming._


    //          .map(i => ((i.payload \ "postcode").as[String], 1))
    //          f.reduceByKeyAndWindow((a:Int, b: Int) => a+b , Minutes(3),Seconds(30))


    //          f.print()

    //          f.publish("hazelcast", Map("ttl" -> "120000"))


  }


}
