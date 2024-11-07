package com.hivemind.app.server

import com.hivemind.app.model.Book
import com.hivemind.app.repository.BookRepository
import io.circe.*
import zio.*
import zio.http.*

class MyRoutes(bookRepo: BookRepository) {

  private val homeRoute: Route[Any, Nothing] =
    Method.GET / Root -> handler(Response.text("Hello World!"))

  private val jsonRoute: Route[Any, Nothing] =
    Method.GET / "json" -> handler(Response.json("""{"greetings": "Hello World!"}"""))

  private val jsonBooks: Route[Any, Nothing] = {

    // get all books from the repository
    val myBooksEffect: UIO[Vector[Book]] = bookRepo.getBooks

    Method.GET / "books" -> handler {
      // Provide a ZIO effect in the handler that runs the effect and converts the result to JSON
      myBooksEffect.map { books =>
        // Encode books Vector into a JSON array
        val myJsonBooks: Json = Encoder.encodeVector[Book].apply(books)

        // use JSON array to generate a string
        val myJsonBooksString: String = myJsonBooks.toString // or use noSpaces for a compact representation

        Response.json(myJsonBooksString)
      }
    }
  }

  val myRoutes: Routes[Any, Nothing] = Routes(homeRoute, jsonRoute, jsonBooks)
}
