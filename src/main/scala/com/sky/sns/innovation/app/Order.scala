package com.sky.sns.innovation.app

import play.api.libs.json.{Json, JsValue}
/**
  * Created by dev on 22/01/16.
  */
case class Order(id: Long, postcode: String, operator: String, ordertype: String, status: String )
case class FactWithId(id: String,
facttype:String,
version:String,
time:String,
source:String,
acquisitiontime:String,
payload:JsValue
                     )
case class MyFlight(origin: Option[String], destination: Option[String], date: Option[String], currency: Option[String], price: Option[Long])

case class Response(currency: String, currPrecision: Long, trips: Seq[Trip], serverTimeUTC: String)
case class Trip(origin: String,destination: String, dates: Seq[Dates])
case class Dates(dateOut: String, flights: Seq[Flight])

case class Flight(flightNumber: String,
                  time: Seq[String],
                  timeUTC: Seq[String],
                  duration: String,
                  faresLeft: Long, flightKey: String,
                  infantsLeft: Long,
                  regularFare: RegularFare,
                  businessFare: RegularFare
                 )
case class RegularFare(fareKey: String, fareClass: String, fares: Seq[Fare])
case class Fare( `type`: String, amount: Double, count: Long, hasDiscount: Boolean, publishedFare: Double )

object Flights{


  import play.api.libs.functional.syntax._
  import play.api.libs.json.{JsPath, Reads}

  implicit val FareReads: Reads[Fare] = (
    (JsPath \ "type").read[String] and
      (JsPath \ "amount").read[Double] and
      (JsPath \ "count").read[Long] and
      (JsPath \ "hasDiscount").read[Boolean] and
      (JsPath \ "publishedFare").read[Double]
    )(Fare.apply _)

  implicit val RegularFareReads: Reads[RegularFare] = (
    (JsPath \ "fareKey").read[String] and
      (JsPath \ "fareClass").read[String] and
      (JsPath \ "fares").read[Seq[Fare]]
    )(RegularFare.apply _)

  implicit val FlightsReads: Reads[Flight] = (
    (JsPath \ "flightNumber").read[String] and
      (JsPath \ "time").read[Seq[String]] and
      (JsPath \ "timeUTC").read[Seq[String]] and
      (JsPath \ "duration").read[String] and
      (JsPath \ "faresLeft").read[Long] and
      (JsPath \ "flightKey").read[String] and
      (JsPath \ "infantsLeft").read[Long] and
      (JsPath \ "regularFare").read[RegularFare] and
      (JsPath \ "businessFare").read[RegularFare]
    )(Flight.apply _)

  implicit val datesReads: Reads[Dates] = (
    (JsPath \ "dateOut").read[String] and
      (JsPath \ "flights").read[Seq[Flight]]
    )(Dates.apply _)

  implicit val tripReads: Reads[Trip] = (
    (JsPath \ "origin").read[String] and
      (JsPath \ "destination").read[String] and
      (JsPath \ "dates").read[Seq[Dates]]
    )(Trip.apply _)

  implicit val responseReads: Reads[Response] = (
      (JsPath \ "currency").read[String] and
      (JsPath \ "currPrecision").read[Long] and
      (JsPath \ "trips").read[Seq[Trip]] and
      (JsPath \ "serverTimeUTC").read[String]
    )(Response.apply _)




  def read(input: String) = Json.parse(input).as[Response]
}