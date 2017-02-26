package com.cornfluence

package object proteus {

  val api = "_api"


  val DELETE = "DELETE"

  def errorMessage(message: Option[String]) = {
    message.getOrElse("")
  }

  implicit val proteusEC = new ProteusExecutionContext().ec

}
