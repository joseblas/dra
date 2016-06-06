package com.sky.sns.innovation

import com.datastax.spark.connector.cql.CassandraConnector
import com.sky.sns.magpie.api.Fact
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import play.api.libs.json.Json

import scala.concurrent.duration.FiniteDuration

class NCStreamingContext(val ncConfig:DConfig, interval:FiniteDuration) extends Serializable {

  println(s"App: ${ncConfig.applicationName}")
  println(s"Master: ${ncConfig.master}")
  println(s"C Server: ${ncConfig.cassandraServer}")
  println(s"C Port: ${ncConfig.cassandraPort}")

  @transient private lazy val sparkConf = new SparkConf()
    .setAppName(ncConfig.applicationName)
    .setMaster(ncConfig.master)
     .set("spark.cassandra.connection.host", ncConfig.cassandraServer)
     .set("spark.cassandra.connection.port", ncConfig.cassandraPort)
  @transient private lazy val streamingContext = new StreamingContext(sparkConf, Seconds(interval.toSeconds))
//  @transient private lazy val sc = new SparkContext(sparkConf)
  /**
   * Create a stream from a topic.
    *
    * @param factType
   * @param version
   * @return
   */
  def stream(factType:String, version:String):DStream[Fact] = {
    // Change to zookeeper address instead???
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> ncConfig.kafkaServers.mkString(","),
      "group.id" -> ncConfig.applicationName,
      "client.id" -> (ncConfig.applicationName).concat("2"),
      "auto.offset.reset" -> "largest"
    )

    initWall

    val topic = factType + "_" + version
    KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](streamingContext, kafkaParams,Set(topic))
      .map {
        case (_, value) =>
          Json.parse(value).as[Fact]
      }
  }



// for DSE: dsetool create_core ras.eorders generateResources=true reindex=true
  def initWall = {
    CassandraConnector(sparkConf).withSessionDo { session =>
      session.execute(s"CREATE KEYSPACE IF NOT EXISTS ras WITH REPLICATION = {'class': 'NetworkTopologyStrategy', 'Solr':2 }") // DC name: Solr, 2 instances replication
      session.execute(s"CREATE TABLE IF NOT EXISTS ras.messages (id TEXT, facttype TEXT, version TEXT, time TEXT, source TEXT, acquisitionTime TEXT, payload TEXT, PRIMARY KEY (id, time, facttype, version, source))")
      session.execute(s"CREATE TABLE IF NOT EXISTS ras.vuelos (flightNumber TEXT, leaves TEXT, arrives TEXT, duration TEXT, regular bigint, business bigint, origin TEXT, destination TEXT,  PRIMARY KEY (flightNumber, leaves))")
    }

  }




  def start() = streamingContext.start()

  def stop(gracefully:Boolean = true) = streamingContext.stop(true, gracefully)

  def awaitTermination() = streamingContext.awaitTermination()
  def awaitTermination(duration:FiniteDuration) = streamingContext.awaitTerminationOrTimeout(duration.toMillis)

}
