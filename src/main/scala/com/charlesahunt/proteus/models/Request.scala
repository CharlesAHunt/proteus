package com.charlesahunt.proteus.models

case class Edge(
  `type` : String,
  _from: String,
  _to: String
)

case class Database(
  name: String,
  users: Option[List[User]]
)

case class Documents(
  documents: List[String]
)

case class CollectionName(
  collection: String
)

case class User(
  username: String,
  password: String,
  active: Boolean = true,
  extra: Option[String] = None
)

//TODO: more attributes here
case class Collection(
  name: String
)

case class Graph(
  name : String,
  edgeDefinitions : List[EdgeDefinition]
)

case class EdgeDefinition(
  collection : String,
  from : List[String],
  to : List[String]
)