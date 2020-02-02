package com.charlesahunt.proteus.models

final case class ArangoError(
  code: Int,
  error: Boolean,
  errorMessage: String,
  errorNum: Int
)


final case class ProteusError(
  errorMessage: String,
)