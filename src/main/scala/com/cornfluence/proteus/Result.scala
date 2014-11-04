package com.cornfluence.proteus


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
                  passwd: String,
                  active: Boolean = true,
                  extra: Option[String] = None
                  )
