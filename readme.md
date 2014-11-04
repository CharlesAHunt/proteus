# Proteus

ArangoDB driver for Scala.

The word 'Proteus' comes the adjective protean, with the general meaning of "versatile", "mutable", "capable of assuming many forms". "Protean" has positive connotations of flexibility, versatility and adaptability. 
The name Proteus is a nod to the versatile and many-formed nature of ArangoDB.

## Install

sbt: libraryDependencies += "com.cornfluence" % "proteus_2.11" % "0.2.2"
maven:      <dependency>
              <groupId>com.cornfluence</groupId>
              <artifactId>proteus_2.11</artifactId>
              <version>0.2.2</version>
              <classifier>sources</classifier>
            </dependency>

## Examples

Create a database:

            val driver = new Driver(databaseName = "test")
            val result = driver.getDatabaseList
            
Create a document (returning the document id as a string):
            
            val driver = new Driver(databaseName = "test")
            val result : String = driver.createDocument("test","testCollection","""{ "Hello": "World" }""")
            
Fetch all documents:

            val driver = new Driver(databaseName = "test")
            val result = driver.getAllDocuments("test", "testCollection")
            
Fetch a single document:

            val driver = new Driver(databaseName = "test")
            val result = driver.getDocument("test", "testCollection", "documentID")
            
Remove a document:

            val driver = new Driver(databaseName = "test")
            val result = driver.removeDocument("test", "testCollection", "documentID")
            
Delete a database:

            val driver = new Driver(databaseName = "test")
            val result = driver.deleteDatabase("test")
