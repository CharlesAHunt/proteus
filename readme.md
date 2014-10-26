# Proteus

ArangoDB driver for Scala.

The word 'Proteus' comes the adjective protean, with the general meaning of "versatile", "mutable", "capable of assuming many forms". "Protean" has positive connotations of flexibility, versatility and adaptability. 
The name Proteus is a nod to the versatile and many-formed nature of ArangoDB.

## Install

ArangoDB is not yet in a public nexus. Coming soon...


## Examples

Create a database:

            val driver = new Driver(databaseName = "test")
            val result = driver.getDatabaseList
            
Create a document:
            
            val driver = new Driver(databaseName = "test")
            val result = driver.createDocument("test","testCollection","""{ "Hello": "World" }""")
