package com.sky.sns.innovation.publishers

case class PublisherInfo(id:String, clazz:Class[Publisher], settings:com.typesafe.config.Config)
