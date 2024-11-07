package com.hivemind.app

import com.hivemind.app.logging.Logger
import com.hivemind.app.repository.BookRepository
import com.hivemind.app.server.MyHttpServer
import zio.http.Server
import zio.{ZIO, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

  private val myAppLogic: ZIO[Logger & Server & MyHttpServer & BookRepository, Throwable, Nothing] =
    for {
      logging  <- ZIO.service[Logger]
      _        <- logging.log("Starting application...")
      _        <- logging.log("Server running at http://localhost:8080")
      myServer <- ZIO.service[MyHttpServer]
      nothing  <- myServer.startServer
    } yield nothing

  override def run: ZIO[Any, Throwable, Nothing] =
    myAppLogic.provide(
      Logger.live,
      MyHttpServer.live,
      Server.default, // You can also provide a custom server configuration
      BookRepository.live,
//      ZLayer.Debug.mermaid, // (uncomment to show a dependency graph in a diagram)
//      ZLayer.Debug.tree, // (uncomment to show a dependency graph in console)
    )
}
