package com.charlesahunt

import com.charlesahunt.proteus.models.ResultMessage

import scala.concurrent.ExecutionContextExecutorService

package object proteus {

  val api = "_api"
  val db = "_db"
  val gharial = "gharial"

  val DELETE = "DELETE"

  def errorMessage(message: Option[String]): String = message.getOrElse("")

  def error(message: String) = Left(new Exception(message))

  def isError(result: ResultMessage): Boolean = result.error.getOrElse(false)

  implicit val proteusEC: ExecutionContextExecutorService = new ProteusExecutionContext().ec

}
