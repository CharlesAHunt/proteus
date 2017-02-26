package com.cornfluence.proteus.models

case class Stats(
  scannedIndex : Int,
  filtered : Int
)

case class Edge(
  _id: String,
  _rev: String,
  _key: String,
  _from: String,
  _to: String,
  $label: String
)

case class Database(
  name: String,
  users: Option[List[User]]
)

case class Documents(
  documents: List[String]
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