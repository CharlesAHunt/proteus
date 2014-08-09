package com.cornfluence.proteus

/**
 *
 * Created by charlesahunt on 7/29/14.
 */

import dispatch._, Defaults._

class Driver(hostMachine : String = "localhost", port : Int = 8529, https : Boolean = false, databaseName : String) {

//	 val connectionTimeout
//	 val socketTimeout
//	 val keepAliveTimeout
//	 val maxTotalConnection
//	 val maxPerConnection
//	 val batchSize
//	 val staleConnectionCheck
//	 val socketKeepAlive

  val arrangoHost = if(https)host(hostMachine, port).secure else host(hostMachine,port)

	val connection = None

	def isConnection():Boolean = {
		if(connection.isEmpty) false else true
	}

  def getDatabases():String = {
    val path =  arrangoHost / "_api" / "database"
    val req = path.GET
    val result = Http(req OK as.String)
    result.onSuccess { case s => return s}
    result.onFailure { case e => e.printStackTrace}
    ""
  }
}

case class Result(
  result : List[String],
  error : Boolean,
  code : Int
)