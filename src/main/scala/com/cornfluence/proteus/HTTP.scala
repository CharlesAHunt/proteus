package com.cornfluence.proteus

import scalaj.http.HttpRequest

object HTTP {

//  def handleResponse[T](response: HttpResponse[T]) = {
//    response.code match {
//
//    }
//  }

  def authToken(request: HttpRequest, jwt: String): HttpRequest =
    request.header("Authorization",s"bearer $jwt")


  case class Auth(username: String, password: String)
  case class JWT(jwt: String, must_change_password: Boolean)

}
