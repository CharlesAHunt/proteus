package com.cornfluence.proteus

/**
 *
 * Created by charlesahunt on 7/29/14.
 */

class Driver(host : String = "localhost", port : String = 8529, https : Boolean = false, databaseName : String) {

//	 val connectionTimeout
//	 val socketTimeout
//	 val keepAliveTimeout
//	 val maxTotalConnection
//	 val maxPerConnection
//	 val batchSize
//	 val staleConnectionCheck
//	 val socketKeepAlive

	val connection = None

	def isConnection():Boolean = {
		if(connection.isEmpty) false else true
	}

}