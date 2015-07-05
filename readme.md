# Proteus

ArangoDB driver for Scala.

The word 'Proteus' comes the adjective protean, with the general meaning of "versatile", "mutable", "capable of assuming many forms". "Protean" has positive connotations of flexibility, versatility and adaptability. 
The name Proteus is a nod to the versatile and many-formed nature of ArangoDB.

## To use

You may need to add the Sonatype nexus to your resolvers:
``` 
 resolvers ++= Seq("OSS" at "http://oss.sonatype.org/content/repositories/releases")
```

sbt:
```
libraryDependencies += "com.cornfluence" % "proteus_2.11" % "0.4.0"
```

maven:   
```
<dependency>
  <groupId>com.cornfluence</groupId>
  <artifactId>proteus_2.11</artifactId>
  <version>0.4.0</version>
  <classifier>sources</classifier>
</dependency>
```

## Examples

###Ops

All non-GET methods return a 'Future[Either[Error,String]]', whereas the String is the success message, and the Error contains all error information.

GET methods return a 'Future[String]' representing a potential individual data object or a 'Future[List[String]]'.

###Database Ops

Create a database:

            val client = DocumentClient(name = "test")
            val result = client.getDatabaseList
            
Delete a database:
            
            client.deleteDatabase("test")
            
###Document Ops
                        
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
            
###Graph Ops

Use an edge client (You can reuse the same database for edges)

            val client = EdgeClient(name = "test")
            
Create an edge (returning the edge id as a string):
            
            client.createEdge("test","testCollection","""{ "Hello": "World" }""","fromCollection","toCollection","fromVertice","toVertice")

Fetch all edges:

            client.getAllEdges("test", "testCollection", "verticeToStartFrom", "directionToTraverse")
            
Fetch a single edge:

            client.getEdge("test", "testCollection", "edgeID")
            
Update/Replace an edge:

            client.replaceEdge("test", "testCollection", "documentID","""{ "Hello": "World" }""")
            
Remove an edge:

            client.deleteEdge("test", "testCollection", "edgeID")
