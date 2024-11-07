package com.hivemind.app.model

case class Book(title: String, author: String, year: Int)

object Book {
  import io.circe.*
  import io.circe.generic.semiauto.*

  given Encoder[Book] = deriveEncoder
  given Decoder[Book] = deriveDecoder
}
