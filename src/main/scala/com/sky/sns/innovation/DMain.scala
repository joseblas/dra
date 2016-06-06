package com.sky.sns.innovation

import com.typesafe.scalalogging.slf4j.LazyLogging


object DMain extends App with LazyLogging
{

  val config = DConfig.default

  val app = config.applicationClass.getConstructor().newInstance()
  val context = new NCStreamingContext(config, app.interval)
//
  logger.info("Starting Draper")
  logger.info("Starting Draper "+ app.interval)

  //
  app.start(context, context.ncConfig)
  context.start()




  context.awaitTermination()


  logger.info("Terminating Draper")
  print("Sky Spark!")


  override def main(args: Array[String]) {
    super.main(args)
  }
}
