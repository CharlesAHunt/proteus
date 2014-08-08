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

  def getDatabases() = {
    val req = arrangoHost.GET
      //.setBody("")
      .addQueryParameter("commit", "true")
      .addHeader("Content-type", "application/json")

    val result = Http(req OK as.String).either
    result match {
      case content         => println("Content: " + content)
      case StatusCode(404) => println("Not found")
      case StatusCode(code) => println("Some other code: " + code.toString)
    }
  }
  
}