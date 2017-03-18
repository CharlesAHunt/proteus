package com.cornfluence.proteus.models

case class Result(
  name : String,
  id : String,
  path : String,
  isSystem : Boolean
)

case class ResultMessage(
  error: Option[Boolean],
  errorMessage: Option[String] = None,
  result: Option[Boolean] = None,
  graph: Option[GraphResponse] = None,
  vertex: Option[EdgeOrVertex] = None,
  edge: Option[EdgeOrVertex] = None,
  code: Option[Int] = None,
  errorNum: Option[Int] = None,
  _id: Option[String] = None,
  _rev: Option[String] = None,
  _key: Option[String] = None,
  _oldRev : Option[String] = None
)

case class EdgeOrVertex(
  _id: String,
  _rev: String,
  _key: String
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
  code: Int,
  hasMore: Option[Boolean],
  cached: Option[Boolean],
  extra: Option[Extra]
)

case class Extra(
  stats: Stats,
   warnings:Option[List[String]]
  )

case class Stats(
  writesExecuted:Long,
  writesIgnored:Long,
  scannedFull:Long,
  scannedIndex:Long,
  filtered:Long
)

case class CurrentDatabase(
  result : Result,
  error : Boolean,
  code : Int
)

case class Error(
  message: String
)

case class CollectionResponse(
  id : String,
  error : Boolean,
  code : Int,
  name : Option[String],
  waitForSync : Option[Boolean],
  isVolatile : Option[Boolean],
  isSystem : Option[Boolean],
  status : Option[Int],
  `type` : Option[Int],
  errorNum: Option[Int],
  errorMessage: Option[String]
)

case class GraphResponse(
  name: String,
  edgeDefinitions: List[EdgeDefinition],
  orphanCollections: List[String],
  isSmart: Boolean,
  numberOfShards: Long,
  smartGraphAttribute: String,
  _id: String,
  _rev: String
)

case class DropGraphResponse(
  error:Boolean,
  removed:Boolean,
  code:Int
)