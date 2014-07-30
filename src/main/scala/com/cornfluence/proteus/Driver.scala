package com.cornfluence.proteus

/**
 *
 * Created by charlesahunt on 7/29/14.
 */

class Driver(connectionURL : String = "http://localhost:8529/", databaseName : String) {

	val connection = None //todo
	val connectionMap: Map[String,String] = Map("connectionURL" -> connectionURL, "dbName" -> databaseName)

	def isConnection():Boolean = {
		if(connection.isEmpty) false else true
	}

}