package com.sky.sns.innovation

import com.sky.sns.innovation.publishers.{Publisher, PublisherInfo}
import com.typesafe.config._

import scala.collection.JavaConversions._

case class DConfig(
                    applicationName:String,
                    applicationClass:Class[NCStreamingApp],
                    master:String,
                    kafkaServers:Set[String],
                    cassandraServer:String,
                    cassandraPort:String,
                    publisherRegistry:Map[String, PublisherInfo]
)

object DConfig {

  lazy val default = fromConfig(ConfigFactory.load())

  def fromConfig(config:Config):DConfig = {

    config.checkValid(ConfigFactory.defaultReference(), "draper")

    //non-lazy fields, we want all exceptions at construct time
    val applicationName = config.getString("draper.application.name")


    val master:String = config.getString("draper.master")

    val kafkaServers:Set[String] = config.getStringList("draper.kafka.servers").toSet
    val cassandraServer = config.getString("draper.application.cassandraHost")
    val cassandraPort = config.getString("draper.application.cassandraPort")


    val applicationClass = Class.forName(config.getString("draper.application.class")).asInstanceOf[Class[NCStreamingApp]]

    val publisherRegistry = config.getConfigList("draper.publishers").map { pubConfig =>
      val config = pubConfig.getConfig("config")
      val id = pubConfig.getString("id")
      val clazz = Class.forName(pubConfig.getString("class")).asInstanceOf[Class[Publisher]]
      (id, PublisherInfo(id, clazz, config))
    }.toMap



    new DConfig(applicationName, applicationClass, master, kafkaServers, cassandraServer, cassandraPort, publisherRegistry)
  }
}
