package com.cornfluence.proteus.models

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

case class Edges(
  edges: List[Edge],
  error : Boolean,
  code : Int,
  stats : Stats
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

case class Error(
  message: String
)

case class CreatedCollection(
  id : String,
  name : String,
  waitForSync : Boolean,
  isVolatile : Boolean,
  isSystem : Boolean,
  status : Int,
  `type` : Int,
  error : Boolean,
  code : Int
)