package com.sky.sns.innovation

import com.sky.sns.innovation.app.HeatMap
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.LazyLogging
/**
  * Created by dev on 21/01/16.
  */
object DLocalMain extends App with LazyLogging
{

  val config = DConfig.fromConfig(ConfigFactory.parseFile(new java.io.File("config/local/application.conf")))

  logger.info("Starting Draper "+ config)
//  val app = config.applicationClass.getConstructor().newInstance()
  val app2 = new HeatMap()

  val context = new NCStreamingContext(config, app2.interval)

//  app.start(context, context.ncConfig)
  app2.start(context, context.ncConfig)

  context.start()



  context.awaitTermination()


  logger.info("Terminating Draper")



  override def main(args: Array[String]) {
    super.main(args)
  }
}