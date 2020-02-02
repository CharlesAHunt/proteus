package com.charlesahunt

import com.charlesahunt.proteus.models.ResultMessage

import scala.concurrent.ExecutionContextExecutorService

package object proteus {

  val api: String = "_api"
  val db: String = "_db"
  val gharial: String = "gharial"

  val DELETE: String = "DELETE"

  def errorMessage(message: Option[String]): String = message.getOrElse("")

  def error[R](message: String): Left[Exception, R] = Left(new Exception(message))

  def isError(result: ResultMessage): Boolean = result.error.getOrElse(false)

  implicit val proteusEC: ExecutionContextExecutorService = new ProteusExecutionContext().ec

}
