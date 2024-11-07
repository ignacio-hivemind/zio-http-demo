package com.hivemind.app.repository

import com.hivemind.app.model.Book
import zio.{UIO, ULayer, ZLayer}

trait BookRepository {
  def getBooks: UIO[Vector[Book]]
}

object BookRepository {
  val live: ULayer[BookRepository] = ZLayer.succeed(new BookRepositoryImpl())

  // It is the same config, but could be a different one
  val testConfig: ULayer[BookRepository] = live
}
