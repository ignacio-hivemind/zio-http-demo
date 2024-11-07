package com.hivemind.app.repository

import com.hivemind.app.model.Book
import zio.{UIO, ZIO}

class BookRepositoryImpl extends BookRepository {

  override def getBooks: UIO[Vector[Book]] = ZIO.succeed(
    Vector(
      Book("The Catcher in the Rye", "J.D. Salinger", 1951),
      Book("To Kill a Mockingbird", "Harper Lee", 1960),
      Book("1984", "George Orwell", 1949),
      Book("Pride and Prejudice", "Jane Austen", 1813),
      Book("The Great Gatsby", "F. Scott Fitzgerald", 1925),
      Book("The Diary of Anna Frank", "Anna Frank", 1947),
      Book("The Hobbit", "J.R.R. Tolkien", 1937),
    ),
  )
}
