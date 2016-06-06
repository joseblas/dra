package com.sky.sns.innovation

import scala.concurrent.duration.{FiniteDuration, _}

/**
 * Each Neo Cortex application has to implement this trait.
 * The application entry point is the start method, by default
 * the data are processed with an interval of 1 second.
 * If you want to specify a different execution interval the
 * attribute interval needs to be redefined.
 */
trait NCStreamingApp {

  val interval:FiniteDuration = 30 second

  def start(implicit context:NCStreamingContext, config:DConfig):Unit

}
