# Proteus

[![Maven Central](https://img.shields.io/maven-central/v/com.cornfluence/proteus_2.12.svg)](https://maven-badges.herokuapp.com/maven-central/com.cornfluence/proteus_2.12)

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
libraryDependencies += "com.cornfluence" % "proteus_2.12" % "0.6.7"
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

## Configuration

To configure your application's ArangoDB user, you will need to add the following to your application.conf
```
 proteus {
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




{
    "service": "timesheet",
    "version": "1.0.0",
    "responseDate": "2018-09-20T15:28:57.319-04:00",
    "users": [
        {
            "userId": 11563,
            "email": "ad7477@intl.att.com",
            "firstName": "ALLARD",
            "lastName": "DE BOER",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ad7477"
        },
        {
            "userId": 15171,
            "email": "ab273e@intl.att.com",
            "firstName": "ALZBETA",
            "lastName": "BOHME RYSKOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ab273e"
        },
        {
            "userId": 15171,
            "email": "ab273e@intl.att.com",
            "firstName": "ALZBETA",
            "lastName": "BOHME RYSKOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ab273e"
        },
        {
            "userId": 14689,
            "email": "ar9523@intl.att.com",
            "firstName": "ANDREA",
            "lastName": "ROBOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ar9523"
        },
        {
            "userId": 12566,
            "email": "anibal.erich.bohme.de.vidts@intl.att.com",
            "firstName": "ANIBAL ERICH",
            "lastName": "BOHME DE VIDTS",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ab436w"
        },
        {
            "userId": 14258,
            "email": "bo096q@intl.att.com",
            "firstName": "BABATUNDE",
            "lastName": "OLAITAN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bo096q"
        },
        {
            "userId": 13599,
            "email": "bb4766@intl.att.com",
            "firstName": "BARBORA",
            "lastName": "BELOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bb4766"
        },
        {
            "userId": 9064,
            "email": "bj808w@intl.att.com",
            "firstName": "BARBORA",
            "lastName": "JAMNICKA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bj808w"
        },
        {
            "userId": 9064,
            "email": "bj808w@intl.att.com",
            "firstName": "BARBORA",
            "lastName": "JAMNICKA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bj808w"
        },
        {
            "userId": 14211,
            "email": "bc529q@intl.att.com",
            "firstName": "BHARGO",
            "lastName": "CHAKRABORTY",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bc529q"
        },
        {
            "userId": 14211,
            "email": "bc529q@intl.att.com",
            "firstName": "BHARGO",
            "lastName": "CHAKRABORTY",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bc529q"
        },
        {
            "userId": 11073,
            "email": "bd644p@intl.att.com",
            "firstName": "BO ERIK",
            "lastName": "DANIELSSON",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bd644p"
        },
        {
            "userId": 15020,
            "email": "br020n@us.att.com",
            "firstName": "BOB",
            "lastName": "RUTKOWSKI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "br020n"
        },
        {
            "userId": 10984,
            "email": "bc3960@intl.att.com",
            "firstName": "BOGDAN",
            "lastName": "CHIRITA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bc3960"
        },
        {
            "userId": 11911,
            "email": "sb089c@us.att.com",
            "firstName": "BONGWOOK",
            "lastName": "JANG",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bj1931"
        },
        {
            "userId": 65,
            "email": "bm004r@us.att.com",
            "firstName": "BONITA",
            "lastName": "MOON",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bm004r"
        },
        {
            "userId": 65,
            "email": "bm004r@us.att.com",
            "firstName": "BONITA",
            "lastName": "MOON",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bm004r"
        },
        {
            "userId": 11868,
            "email": "boonchiun.khoo@intl.att.com",
            "firstName": "BOON CHIUN",
            "lastName": "KHOO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bk8773"
        },
        {
            "userId": 11868,
            "email": "boonchiun.khoo@intl.att.com",
            "firstName": "BOON CHIUN",
            "lastName": "KHOO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bk8773"
        },
        {
            "userId": 14680,
            "email": "bi723n@intl.att.com",
            "firstName": "BORIS",
            "lastName": "IVAN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bi723n"
        },
        {
            "userId": 14680,
            "email": "bi723n@intl.att.com",
            "firstName": "BORIS",
            "lastName": "IVAN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "bi723n"
        },
        {
            "userId": 2393,
            "email": "bo051k@us.att.com",
            "firstName": "BRIAN",
            "lastName": "OSBORNE",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bo051k"
        },
        {
            "userId": 2393,
            "email": "bo051k@us.att.com",
            "firstName": "BRIAN",
            "lastName": "OSBORNE",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "bo051k"
        },
        {
            "userId": 793,
            "email": "cb234s@us.att.com",
            "firstName": "CHRISTOPHER",
            "lastName": "BOTIMER",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "cb234s"
        },
        {
            "userId": 3180,
            "email": "dana.sabolova@intl.att.com",
            "firstName": "DANA",
            "lastName": "MORAVKOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ds2524"
        },
        {
            "userId": 3180,
            "email": "dana.sabolova@intl.att.com",
            "firstName": "DANA",
            "lastName": "MORAVKOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ds2524"
        },
        {
            "userId": 2050,
            "email": "dc687u@us.att.com",
            "firstName": "DAVID",
            "lastName": "CIRBO",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "dc687u"
        },
        {
            "userId": 14601,
            "email": "dc2784@us.att.com",
            "firstName": "DEBORAH",
            "lastName": "CARDONE",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "dc2784"
        },
        {
            "userId": 4283,
            "email": "dt164t@us.att.com",
            "firstName": "DEBORAH",
            "lastName": "TOY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "dt164t"
        },
        {
            "userId": 4283,
            "email": "dt164t@us.att.com",
            "firstName": "DEBORAH",
            "lastName": "TOY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "dt164t"
        },
        {
            "userId": 10015,
            "email": "db174s@intl.att.com",
            "firstName": "DUSAN",
            "lastName": "BORSOVSKY",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "db174s"
        },
        {
            "userId": 10015,
            "email": "db174s@intl.att.com",
            "firstName": "DUSAN",
            "lastName": "BORSOVSKY",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "db174s"
        },
        {
            "userId": 12719,
            "email": "eb383e@intl.att.com",
            "firstName": "ELMI",
            "lastName": "BOUH",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "eb383e"
        },
        {
            "userId": 12528,
            "email": "eric.boiton@intl.att.com",
            "firstName": "ERIC",
            "lastName": "BOITON",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "eb574w"
        },
        {
            "userId": 12528,
            "email": "eric.boiton@intl.att.com",
            "firstName": "ERIC",
            "lastName": "BOITON",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "eb574w"
        },
        {
            "userId": 14955,
            "email": "fb903p@intl.att.com",
            "firstName": "FEDERICO",
            "lastName": "BORDOLI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "fb903p"
        },
        {
            "userId": 11297,
            "email": "fbohnen@intl.att.com",
            "firstName": "FRANK",
            "lastName": "BOHNEN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "fb1512"
        },
        {
            "userId": 11668,
            "email": "ml175b@att.com",
            "firstName": "GABOR",
            "lastName": "TAKACS",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "gt7388"
        },
        {
            "userId": 264,
            "email": "ml175b@att.com",
            "firstName": "GARRY",
            "lastName": "BOCKHOLT",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "gb037p"
        },
        {
            "userId": 15200,
            "email": "gb4321@us.att.com",
            "firstName": "GARY",
            "lastName": "BOLLMANN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "gb4321"
        },
        {
            "userId": 11547,
            "email": "gd3388@us.att.com",
            "firstName": "GARY",
            "lastName": "DUBOSE",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "gd3388"
        },
        {
            "userId": 10860,
            "email": "gl032p@intl.att.com",
            "firstName": "GILLES",
            "lastName": "LEBORGNE",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "gl032p"
        },
        {
            "userId": 10860,
            "email": "gl032p@intl.att.com",
            "firstName": "GILLES",
            "lastName": "LEBORGNE",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "gl032p"
        },
        {
            "userId": 14187,
            "email": "sb089c@us.att.com",
            "firstName": "IVANA",
            "lastName": "DUBOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "id004s"
        },
        {
            "userId": 14187,
            "email": "sb089c@us.att.com",
            "firstName": "IVANA",
            "lastName": "DUBOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "id004s"
        },
        {
            "userId": 10722,
            "email": "ig182u@intl.att.com",
            "firstName": "IVO",
            "lastName": "GRABOWSKI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ig182u"
        },
        {
            "userId": 650,
            "email": "ml175b@att.com",
            "firstName": "JAMES",
            "lastName": "BODDY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "jb063j"
        },
        {
            "userId": 650,
            "email": "ml175b@att.com",
            "firstName": "JAMES",
            "lastName": "BODDY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "jb063j"
        },
        {
            "userId": 643,
            "email": "js905m@us.att.com",
            "firstName": "JEFFREY",
            "lastName": "SIDEBOTHAM",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "js905m"
        },
        {
            "userId": 643,
            "email": "js905m@us.att.com",
            "firstName": "JEFFREY",
            "lastName": "SIDEBOTHAM",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "js905m"
        },
        {
            "userId": 11305,
            "email": "joerg.boettcher@intl.att.com",
            "firstName": "JOERG",
            "lastName": "BOETTCHER",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "jb139b"
        },
        {
            "userId": 11235,
            "email": "jb5570@intl.att.com",
            "firstName": "JOSE",
            "lastName": "BOSSI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "jb5570"
        },
        {
            "userId": 11235,
            "email": "jb5570@intl.att.com",
            "firstName": "JOSE",
            "lastName": "BOSSI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "jb5570"
        },
        {
            "userId": 2015,
            "email": "jb7469@us.att.com",
            "firstName": "JOSEPH",
            "lastName": "BOJANOWSKI",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "jb7469"
        },
        {
            "userId": 14345,
            "email": "kb043p@intl.att.com",
            "firstName": "KAREL",
            "lastName": "BOHM",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "kb043p"
        },
        {
            "userId": 10882,
            "email": "kurt.verboven@intl.att.com",
            "firstName": "KURT",
            "lastName": "VERBOVEN",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "kv2162"
        },
        {
            "userId": 16605,
            "email": "lb544m@intl.att.com",
            "firstName": "LUBOMIR",
            "lastName": "BETKO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "lb544m"
        },
        {
            "userId": 6983,
            "email": "lubomira.eperjesiova@intl.att.com",
            "firstName": "LUBOMIRA",
            "lastName": "EPERJESIOVA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "le404q"
        },
        {
            "userId": 6983,
            "email": "lubomira.eperjesiova@intl.att.com",
            "firstName": "LUBOMIRA",
            "lastName": "EPERJESIOVA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "le404q"
        },
        {
            "userId": 11899,
            "email": "sb089c@us.att.com",
            "firstName": "LUBOMIRA",
            "lastName": "LUKACINOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ll565x"
        },
        {
            "userId": 11899,
            "email": "sb089c@us.att.com",
            "firstName": "LUBOMIRA",
            "lastName": "LUKACINOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ll565x"
        },
        {
            "userId": 3170,
            "email": "lubos.nemcovsky@intl.att.com",
            "firstName": "LUBOS",
            "lastName": "NEMCOVSKY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "ln2876"
        },
        {
            "userId": 3170,
            "email": "lubos.nemcovsky@intl.att.com",
            "firstName": "LUBOS",
            "lastName": "NEMCOVSKY",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "ln2876"
        },
        {
            "userId": 11219,
            "email": "lp247v@intl.att.com",
            "firstName": "LUBOS",
            "lastName": "PALUCH",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "lp247v"
        },
        {
            "userId": 14633,
            "email": "lv331w@intl.att.com",
            "firstName": "LUCIA",
            "lastName": "VYBOSTOKOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "lv331w"
        },
        {
            "userId": 10716,
            "email": "mo879a@intl.att.com",
            "firstName": "MANFRED",
            "lastName": "ORBONNS",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "mo879a"
        },
        {
            "userId": 12718,
            "email": "nb423b@intl.att.com",
            "firstName": "NIVALDO",
            "lastName": "BORALI",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "nb423b"
        },
        {
            "userId": 655,
            "email": "pv470g@intl.att.com",
            "firstName": "PATRICK",
            "lastName": "VANDEBOSCH",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "pv470g"
        },
        {
            "userId": 11518,
            "email": "ml175b@att.com",
            "firstName": "PAVOL",
            "lastName": "BORO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "pb056q"
        },
        {
            "userId": 4570,
            "email": "pavol.bozogan@intl.att.com",
            "firstName": "PAVOL",
            "lastName": "BOZOGAN",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "pb826s"
        },
        {
            "userId": 4570,
            "email": "pavol.bozogan@intl.att.com",
            "firstName": "PAVOL",
            "lastName": "BOZOGAN",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "pb826s"
        },
        {
            "userId": 15198,
            "email": "ps148a@intl.att.com",
            "firstName": "PETR",
            "lastName": "SVOBODA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ps148a"
        },
        {
            "userId": 676,
            "email": "pb050k@us.att.com",
            "firstName": "PHYLLIS",
            "lastName": "BOYNTON",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "pb050k"
        },
        {
            "userId": 676,
            "email": "pb050k@us.att.com",
            "firstName": "PHYLLIS",
            "lastName": "BOYNTON",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "pb050k"
        },
        {
            "userId": 16585,
            "email": "pg317s@intl.att.com",
            "firstName": "PIOTR",
            "lastName": "GOLUMBOVSKIJ",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "pg317s"
        },
        {
            "userId": 187,
            "email": "ml175b@att.com",
            "firstName": "RICHARD",
            "lastName": "BOLLINE",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "rb527p"
        },
        {
            "userId": 187,
            "email": "ml175b@att.com",
            "firstName": "RICHARD",
            "lastName": "BOLLINE",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "rb527p"
        },
        {
            "userId": 4483,
            "email": "rc4090@us.att.com",
            "firstName": "RICHARD",
            "lastName": "CHABOT",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "rc4090"
        },
        {
            "userId": 12672,
            "email": "rl988y@us.att.com",
            "firstName": "RUSSELL",
            "lastName": "LEBOURDAIS",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "rl988y"
        },
        {
            "userId": 1985,
            "email": "sb944k@us.att.com",
            "firstName": "SALVATORE",
            "lastName": "BOLOGNA",
            "managerAttuid": null,
            "middleName": " J",
            "sbcid": "sb944k"
        },
        {
            "userId": 10902,
            "email": "sl670f@intl.att.com",
            "firstName": "SANDRA",
            "lastName": "LA BORDE",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "sl670f"
        },
        {
            "userId": 11366,
            "email": "sb392m@intl.att.com",
            "firstName": "SANDRO",
            "lastName": "BONARRIGO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "sb392m"
        },
        {
            "userId": 17283,
            "email": "ss509g@intl.att.com",
            "firstName": "SEYNABOU",
            "lastName": "SOW",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "ss509g"
        },
        {
            "userId": 3666,
            "email": "sb275n@us.att.com",
            "firstName": "SHAWN",
            "lastName": "BOUDA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "sb275n"
        },
        {
            "userId": 18463,
            "email": "sb089c@us.att.com",
            "firstName": "SIVA",
            "lastName": "BONU",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "sb089c"
        },
        {
            "userId": 18463,
            "email": "sb089c@us.att.com",
            "firstName": "SIVA",
            "lastName": "BONU",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "sb089c"
        },
        {
            "userId": 14626,
            "email": "sb597m@us.att.com",
            "firstName": "STEVEN",
            "lastName": "BONHAM",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "sb597m"
        },
        {
            "userId": 14991,
            "email": "tb153f@intl.att.com",
            "firstName": "TIBOR",
            "lastName": "BARNA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "tb153f"
        },
        {
            "userId": 14991,
            "email": "tb153f@intl.att.com",
            "firstName": "TIBOR",
            "lastName": "BARNA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "tb153f"
        },
        {
            "userId": 17104,
            "email": "tb529v@intl.att.com",
            "firstName": "TOMAS",
            "lastName": "BOHMER",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "tb529v"
        },
        {
            "userId": 3349,
            "email": "tomas.boris@intl.att.com",
            "firstName": "TOMAS",
            "lastName": "BORIS",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "tb565x"
        },
        {
            "userId": 11209,
            "email": "vb842n@intl.att.com",
            "firstName": "VERONIKA",
            "lastName": "BOLFOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "vb842n"
        },
        {
            "userId": 11209,
            "email": "vb842n@intl.att.com",
            "firstName": "VERONIKA",
            "lastName": "BOLFOVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "vb842n"
        },
        {
            "userId": 4568,
            "email": "vojtech.szobonya@intl.att.com",
            "firstName": "VOJTECH",
            "lastName": "SZOBONYA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "vs965m"
        },
        {
            "userId": 4568,
            "email": "vojtech.szobonya@intl.att.com",
            "firstName": "VOJTECH",
            "lastName": "SZOBONYA",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "vs965m"
        },
        {
            "userId": 4988,
            "email": "wb420m@intl.att.com",
            "firstName": "WERNER",
            "lastName": "BOETTCHER",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "wb420m"
        },
        {
            "userId": 8906,
            "email": "wb9550@intl.att.com",
            "firstName": "WILLEM",
            "lastName": "BOTH",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "wb9550"
        },
        {
            "userId": 7163,
            "email": "wb176n@intl.att.com",
            "firstName": "WILLIAM",
            "lastName": "BOUWHUIS",
            "managerAttuid": null,
            "middleName": " ",
            "sbcid": "wb176n"
        },
        {
            "userId": 11417,
            "email": "wl600d@intl.att.com",
            "firstName": "WOLFRAM",
            "lastName": "LEIBOLD",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "wl600d"
        },
        {
            "userId": 12607,
            "email": "zz634v@intl.att.com",
            "firstName": "ZHEN BO",
            "lastName": "ZHAO",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "zz634v"
        },
        {
            "userId": 10908,
            "email": "zb304g@intl.att.com",
            "firstName": "ZUZANA",
            "lastName": "BOLCAROVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "zb304g"
        },
        {
            "userId": 10908,
            "email": "zb304g@intl.att.com",
            "firstName": "ZUZANA",
            "lastName": "BOLCAROVA",
            "managerAttuid": null,
            "middleName": null,
            "sbcid": "zb304g"
        }
    ]
}
