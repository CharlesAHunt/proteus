# Proteus

[![Maven Central](https://img.shields.io/maven-central/v/com.cornfluence/proteus_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.cornfluence/proteus_2.12)
[![Build Status](https://travis-ci.org/charlesahunt/proteus.svg?branch=master)](https://travis-ci.org/charlesahunt/proteus)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/be829fed3c134f8cbf14c60290651d63)](https://www.codacy.com/app/matthicks/proteus?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=charlesahunt/proteus&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/be829fed3c134f8cbf14c60290651d63)](https://www.codacy.com/app/matthicks/proteus?utm_source=github.com&utm_medium=referral&utm_content=cornfluence/proteus&utm_campaign=Badge_Coverage)
[![Stories in Ready](https://badge.waffle.io/cornfluence/proteus.png?label=ready&title=Ready)](https://waffle.io/charlesahunt/proteus)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cornfluence/proteus)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.cornfluence/proteus_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.cornfluence/proteus_2.12)
[![Latest version](https://index.scala-lang.org/cornfluence/proteus/proteus/latest.svg)](https://index.scala-lang.org/cornfluence/proteus)

ArangoDB driver for Scala.

The word 'Proteus' comes the adjective protean, with the general meaning of "versatile", "mutable", "capable of assuming many forms". "Protean" has positive connotations of flexibility, versatility and adaptability. 
The name Proteus is a nod to the versatile and many-formed nature of ArangoDB.

This driver is compatible with ArangoDB `v3.6.x`

## Getting Started

You may need to add the Sonatype nexus to your resolvers:
``` 
 resolvers ++= Seq("OSS" at "http://oss.sonatype.org/content/repositories/releases")
```

sbt:
```
libraryDependencies += "com.cornfluence" % "proteus_2.13" % "0.7.2"
```

maven:
```
<dependency>
  <groupId>com.cornfluence</groupId>
  <artifactId>proteus_2.12</artifactId>
  <version>0.6.7</version>
  <classifier>sources</classifier>
</dependency>
```

Note: Versions of Proteus less than 0.6.0 are for ArangoDB 2.x and built with Scala 2.11

Note: Versions of Proteus greater than 0.6.0 and less than 0.7.0 are for ArangoDB 3.x and built with Scala 2.12

Note: Versions of Proteus greater than 0.7.0 are for ArangoDB 3.6.+ and built with Scala 2.13


## Configuration

To configure your application's ArangoDB user, you will need to add the following to your application.conf
```
 proteus {
    host = "localhost"
    user = "username"       //arangodb default is:  "root"
    password = "password"   //arangodb default is:  ""
 }
```

## Examples

### Client API

            val client = DocumentClient(name = "test")
        
            val client = GraphClient(name = "test")

### Database API

Create a database:

            client.createDatabase("dbName", List(User(username = "user", password = "pass", active = true)))
            
            client.getDatabaseList
            
            client.getCurrentDatabase
            
Delete a database:
            
            client.deleteDatabase("dbName")
            
            
### Collection API

            client.createCollection("dbName", testCollection)
    
            client.dropCollection("dbName", testCollection)

            
### Document API
                        
Create a document (returning the document id as a string):

            client.createDocument("dbName","testCollection","""{ "Hello": "World" }""")
            
Fetch all documents:

            client.getAllDocuments("dbName", "testCollection")
            
Fetch a single document:

            client.getDocument("dbName", "testCollection", "documentID")

Update/Replace a document:
            
            client.replaceDocument("dbName", "testCollection", "documentID","""{ "Hello": "World" }""")
            
Remove a document:

            client.deleteDocument("dbName", "testCollection", "documentID")
            
### Graph API

 (Graph API is still under some development)
 
Create a graph

            client.createGraph("graphName", List())
 
Drop a graph

            client.dropGraph("graphName")
 
Create a vertex collection

            client.createVertexCollection("graphName", "vertexCollectionName")

Create an edge collection

            client.createEdgeCollection("graphName", "edgeCollectionName", List("vertexCollectionName"), List("otherVertexCollectionName"))

Create a vertex

            client.createVertex("graphName", "vertexCollectionName", """{"free":"style"}""")

Create an edge

            client.createEdge("graphName", "edgeCollectionName", "typeName", "vertexOneID", "vertexTwoID")
            
Delete an edge

            client.deleteEdge("graphName", "edgeCollectionName", "edgeKey")

Delete an edge collection

            client.deleteEdgeCollection("graphName", "edgeCollectionName")

Delete a vertex

            client.deleteVertex("graphName", "vertexCollectionName", "vertexKey")

Delete a vertex collection

            client.deleteVertexCollection("graphName", "vertexCollectionName")
