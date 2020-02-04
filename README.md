# Proteus

[![Latest version](https://index.scala-lang.org/charlesahunt/proteus/proteus/latest.svg)](https://index.scala-lang.org/cornfluence/proteus)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.charlesahunt/proteus_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.charlesahunt/proteus_2.13)
[![Proteus](https://circleci.com/gh/CharlesAHunt/proteus.svg?style=svg)](https://circleci.com/gh/CharlesAHunt/proteus)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/be829fed3c134f8cbf14c60290651d63)](https://www.codacy.com/app/matthicks/proteus?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=charlesahunt/proteus&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/be829fed3c134f8cbf14c60290651d63)](https://www.codacy.com/app/matthicks/proteus?utm_source=github.com&utm_medium=referral&utm_content=cornfluence/proteus&utm_campaign=Badge_Coverage)

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
    libraryDependencies += "com.cornfluence" % "proteus_2.13" % "0.7.3"
```

maven:
```
    <dependency>
      <groupId>com.cornfluence</groupId>
      <artifactId>proteus_2.13</artifactId>
      <version>0.7.3</version>
      <classifier>sources</classifier>
    </dependency>
```

Note: Versions of Proteus greater than 0.7.0 are for ArangoDB 3.6.+ and built with Scala 2.13


## Configuration

To configure your application's ArangoDB user, you will need to create a ProteusConfig configuration object to initialize
  the document and graph clients.  You will most likely want to materialize the configuration case class from an application.conf
  that looks like the following so you can materialize it to a case class using `Config.configuration` in your calling code:
```
    proteus {
        host = "localhost"
        user = "root"
        password = "openSesame"
        port = "8529",
        tls = false
    }
```

You can also create the ProteusConfig manually in your source like so:

```
    ProteusConfig(
      host: String = "localhost",
      user: String,
      password: String,
      port: String = "8529",
      tls: Boolean = false
    )
```

## Examples

### Client API
```
    val arangoClient = ArangoClient(config = ProteusConfig(...))

    val documentClient = DocumentClient(config = ProteusConfig(...), databaseName = "testDB")
            
    val graphClient = GraphClient(config = ProteusConfig(...), graphName = "testGraph")

```
### Database API

Create a database:
```
    arangoClient.createDatabase("dbName", List(User(username = "user", password = "pass", active = true)))
    
    arangoClient.getDatabaseList
    
    arangoClient.getCurrentDatabase
```           
Delete a database:
 ```           
    arangoClient.deleteDatabase("dbName")
            
```            
### Collection API
```
    arangoClient.createCollection("dbName", testCollection)
    
    arangoClient.dropCollection("dbName", testCollection)

```            
### Document API
                        
Create a document (returning the document id as a string):
```
    documentClient.createDocument("testCollection","""{ "Hello": "World" }""")
```           
Fetch all documents:
```
    documentClient.getAllDocuments("testCollection")
```         
Fetch a single document:
```
    documentClient.getDocument("testCollection", "documentID")
```
Update/Replace a document:
```            
    documentClient.replaceDocument("testCollection", "documentID","""{ "Hello": "World" }""")
```            
Remove a document:
```
    documentClient.deleteDocument("testCollection", "documentID")
```            
### Graph API

 (Graph API is still under some development)
 
Create a graph
```
    graphClient.createGraph(List())
``` 
Drop a graph
```
    graphClient.dropGraph()
``` 
Create a vertex collection
```
    graphClient.createVertexCollection("vertexCollectionName")
```
Create an edge collection
```
    graphClient.createEdgeCollection("edgeCollectionName", List("vertexCollectionName"), List("otherVertexCollectionName"))
```
Create a vertex
```
    graphClient.createVertex("vertexCollectionName", """{"free":"style"}""")
```
Create an edge
```
    graphClient.createEdge("edgeCollectionName", "typeName", "vertexOneID", "vertexTwoID")
```            
Delete an edge
```
    graphClient.deleteEdge("edgeCollectionName", "edgeKey")
```
Delete an edge collection
```
    graphClient.deleteEdgeCollection("edgeCollectionName")
```
Delete a vertex
```
    graphClient.deleteVertex("vertexCollectionName", "vertexKey")
```
Delete a vertex collection
```
    graphClient.deleteVertexCollection("vertexCollectionName")
```
