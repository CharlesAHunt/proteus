package com.charlesahunt.proteus.utils

import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import com.charlesahunt.proteus.models.{CursorResults}

import scala.concurrent.Future
import com.charlesahunt.proteus.ArangoClient

object Utils {
  def getCursorID(
      queryResultViaCursor: Either[Throwable, String]
  ): Option[String] = {
    queryResultViaCursor match {
      case Left(value) =>
        //println(value)
        None
      case Right(value) =>
        //println(value)
        decode[CursorResults](value) match {
          case Right(ok) =>
            //println(ok)
            if (ok.hasMore.getOrElse(false)) ok.id else None
          case Left(error) =>
            //println(error.getMessage)
            None
        }
    }
  }

  def getCursorResults(
      queryResultViaCursor: Future[Either[Throwable, String]]
  )(
      implicit ec: scala.concurrent.ExecutionContext
  ): Future[Either[Throwable, CursorResults]] = {
    queryResultViaCursor.map(f => {
      f match {
        case Left(value) =>
          Left(value)
        case Right(value) =>
          decode[CursorResults](value) match {
            case Right(ok) =>
              Right(ok)
            case Left(error) =>
              println(error.getMessage)
              Left(error)
          }
      }
    })
  }
}
