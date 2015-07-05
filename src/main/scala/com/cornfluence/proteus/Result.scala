package com.cornfluence.proteus


case class Edge(
   _id : String,
   _rev : String,
   _key : String,
   _from : String,
   _to : String,
   $label : String
)

case class ResultList(
                        result: List[String],
                        error: Boolean,
                        code: Int
                        )

case class ResultMessage(
                           error: Boolean,
                           errorMessage: Option[String],
                           result: Option[Boolean],
                           code: Option[Int],
                           errorNum : Option[Int],
                           _id: Option[String],
                           _rev: Option[String],
                           _key: Option[String]
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
                message : String
                   )
