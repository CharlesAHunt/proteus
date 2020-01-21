package com.charlesahunt.proteus.models

final case class Edge(
  `type` : String,
  _from: String,
  _to: String
)

final case class Database(
  name: String,
  users: Option[List[User]]
)

final case class Documents(
  documents: List[String]
)

final case class CollectionName(
  collection: String
)

final case class User(
  username: String,
  password: String,
  active: Boolean = true,
  extra: Option[String] = None
)

//TODO: more attributes here
final case class Collection(
  name: String
)

final case class Graph(
  name : String,
  edgeDefinitions : List[EdgeDefinition]
)

final case class EdgeDefinition(
  collection : String,
  from : List[String],
  to : List[String]
)