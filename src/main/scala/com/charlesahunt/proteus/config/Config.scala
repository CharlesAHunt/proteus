package com.charlesahunt.proteus.config

import com.typesafe.scalalogging.Logger
import pureconfig._
import pureconfig.generic.auto._

object Config {

  private val logger = Logger.apply("Config")

  private def exit(): Nothing = sys.exit(1)

  val configuration: ProteusConfig = {
    logger.info(s"Loading config")
    ConfigSource.default.load[ProteusConfig] match {
      case Left(error) =>
        logger.error(s"There was an error loading the config, shutting down: ${error.toList.toString()}")
        exit()
      case Right(config) => config
    }
  }

}

final case class ProteusConfig(
  user: String,
  password: String,
  dataBaseName: String,
  host: String = "localhost",
  port: String = "8529",
  tls: Boolean = false
)