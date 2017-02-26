package com.cornfluence

package object proteus {

  val api = "_api"
  val db = "_db"

  val DELETE = "DELETE"

  def errorMessage(message: Option[String]) = {
    message.getOrElse("")
  }

  implicit val proteusEC = new ProteusExecutionContext().ec

}
