package com.cornfluence.proteus

case class Edges(
  edges: List[Edge],
  error : Boolean,
  code : Int,
  stats : Stats
)

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

case class ResultList(
  result: List[String],
  error: Boolean,
  code: Int
)

case class CurrentDatabase(
  result : Result,
  error : Boolean,
  code : Int
)

case class Result(
  name : String,
  id : String,
  path : String,
  isSystem : Boolean
)

case class ResultMessage(
  error: Boolean,
  errorMessage: Option[String] = None,
  result: Option[Boolean] = None,
  code: Option[Int] = None,
  errorNum: Option[Int] = None,
  _id: Option[String] = None,
  _rev: Option[String] = None,
  _key: Option[String] = None
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

case class Error(
  message: String
)