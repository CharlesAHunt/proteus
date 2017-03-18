# Proteus

ArangoDB driver for Scala.

The word 'Proteus' comes the adjective protean, with the general meaning of "versatile", "mutable", "capable of assuming many forms". "Protean" has positive connotations of flexibility, versatility and adaptability. 
The name Proteus is a nod to the versatile and many-formed nature of ArangoDB.

## Getting Started

You may need to add the Sonatype nexus to your resolvers:
``` 
 resolvers ++= Seq("OSS" at "http://oss.sonatype.org/content/repositories/releases")
```

sbt:
```
libraryDependencies += "com.cornfluence" % "proteus_2.12" % "0.6.0"
```

maven:   
```
<dependency>
  <groupId>com.cornfluence</groupId>
  <artifactId>proteus_2.12</artifactId>
  <version>0.6.0</version>
  <classifier>sources</classifier>
</dependency>
```

Note: Versions of Proteus less than 0.6.0 are for ArangoDB 2.x and built with Scala 2.11

## Examples

### Client API

            val client = DocumentClient(name = "test")
        
            val client = GraphClient(name = "test")

### Database API

Create a database:

            client.createDatabase(dbName, List(User(username = "user", password = "pass", active = true)))
            
            client.getDatabaseList
            
            client.getCurrentDatabase
            
Delete a database:
            
            client.deleteDatabase("test")
            
            
### Collection API

            client.createCollection(testDB, testCollection)
    
            client.dropCollection(testDB, testCollection)

            
### Document API
                        
Create a document (returning the document id as a string):

            client.createDocument("test","testCollection","""{ "Hello": "World" }""")
            
Fetch all documents:

            client.getAllDocuments("test", "testCollection")
            
Fetch a single document:

            client.getDocument("test", "testCollection", "documentID")

Update/Replace a document:
            
            client.replaceDocument("test", "testCollection", "documentID","""{ "Hello": "World" }""")
            
Remove a document:

            client.deleteDocument("test", "testCollection", "documentID")
            
### Graph API

 (Graph API is still under some development)
 
Create a graph

            driver.createGraph("graphName", List())
 
Drop a graph

            driver.dropGraph("graphName")
 
Create a vertex collection

            client.createVertexCollection("graphName", "vertexCollectionName")

Create an edge collection

            client.createEdgeCollection("graphName", "edgeCollectionName", List("vertexCollectionName"), List("otherVertexCollectionName"))

Create a vertex

            client.createVertex("graphName", "vertexCollectionName", """{"free":"style"}""")

Create an edge

            client.createEdge("graphName", "edgeCollectionName", "typeName", "vertexOneID", "vertexTwoID")
            
