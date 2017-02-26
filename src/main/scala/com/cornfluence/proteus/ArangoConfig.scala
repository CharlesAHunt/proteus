package com.cornfluence.proteus

import com.typesafe.config.ConfigFactory

trait ArangoConfig {

  val conf = ConfigFactory.load()
  val userName = conf.getString("proteus.user")
  val password = conf.getString("proteus.password")

}
