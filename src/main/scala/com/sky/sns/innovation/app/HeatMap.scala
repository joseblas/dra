package com.sky.sns.innovation.app
//import com.datastax.spark.connector._
//import com.datastax.spark.connector.streaming._
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.streaming._
import com.sky.sns.innovation._
import com.sky.sns.innovation.app.Flights.{read}
import play.api.libs.json.Json

import scala.concurrent.duration._

class HeatMap extends NCStreamingApp {

  /**
    * set the interval of execution
    */
  override val interval = 10 seconds




  /**
    * Application entry point
    *
    * @param context current context of execution
    * @param config current configuration
    */
override def start(implicit context:NCStreamingContext, config:DConfig) = {
//    val rdd = context.stream("flights", "1.0").foreachRDD { rdd =>
//        rdd.foreachPartition{ partition =>
//          partition.map(i =>
//            new Flight
//            (
//
//            )
//          )
//        }
//    }
//      rdd.saveToCassandra("ra", "flights")


    val f = context.stream("flights", "1.0")
      .filter( f => (f.payload \ "message").asOpt[String].isEmpty  )
      .map( i => read(i.payload.toString) )
      .filter( i => i.trips.length > 0 )

         f.print()

    val f2 = f.flatMap( i => (i.trips) )
      .flatMap( i => i.dates )
      .filter(date => date.flights.nonEmpty)
      .flatMap( i => i.flights)
      .map( date => (date.flightNumber, date.time(0), date.time(1), date.duration,
        date.regularFare.fares(0).amount, date.businessFare.fares(0).amount, date.flightKey.substring(11,14), date.flightKey.substring(32,35)  ))


     f2.saveToCassandra("ras", "vuelos", columnNames = SomeColumns("flightnumber","leaves","arrives","duration","regular","business", "origin","destination"))


      /*f.map(i =>
          new Flight
          (
            ((((i.payload \\ "trips")(0))(0) \\ "dates")(0)(0)\ "dateOut") .asOpt[String],
            (((i.payload \\ "trips")(0))(0) \\ "origin")(0).asOpt[String],
            (((i.payload \\ "trips")(0))(0) \\ "destination")(0).asOpt[String],
            (i.payload \ "currency").asOpt[String],
            Option.apply(20L) // ((((((i.payload\\ "trips")(0)) \\ "flights")(0) \\ "regularFare" )(0) \\ "fares")(0) \\ "amount").head.asOpt[Long]
          )
        ).saveToCassandra("ra", "flights")
*/


//      .print()
//      .saveToCassandra("ra", "flights")
//      f.saveToCassandra("ra", "flights")




//

    // import com.datastax.spark.connector.streaming._
//         f.saveToCassandra("orders","message")//, SomeColumns("id","postcode","operator","ordertype"))




//          .map(i => ((i.payload \ "postcode").as[String], 1))
//          f.reduceByKeyAndWindow((a:Int, b: Int) => a+b , Minutes(3),Seconds(30))


//          f.print()

//          f.publish("hazelcast", Map("ttl" -> "120000"))


  }


}
