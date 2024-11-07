package com.hivemind.app.server

import com.hivemind.app.repository.BookRepository
import zio.*
import zio.http.*

class MyHttpServer(val routes: MyRoutes) {
  def startServer: URIO[Server, Nothing] = Server.serve(routes.myRoutes)
}

object MyHttpServer {
  val live: URLayer[BookRepository, MyHttpServer] = ZLayer {
    for {
      bookRepo <- ZIO.service[BookRepository]
      routes   <- ZIO.succeed(new MyRoutes(bookRepo))
    } yield new MyHttpServer(routes)
  }

  // It is the same config, but could be a different one
  val testConfig: URLayer[BookRepository, MyHttpServer] = live
}
