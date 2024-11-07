package com.hivemind.app.server

import com.hivemind.app.model.Book
import com.hivemind.app.repository.BookRepository
import zio.*
import zio.http.*
import zio.http.netty.NettyConfig
import zio.http.netty.server.NettyDriver
import zio.test.*

import scala.language.postfixOps

object MyHttpServerSpec extends ZIOSpecDefault {

  lazy val test1: Spec[TestServer & Client, Throwable] = test("demo test server code") {
    val fixture = new TestConfiguration
    for {
      client           <- ZIO.service[Client]
      port             <- ZIO.serviceWithZIO[Server](_.port)
      testRequest       = Request
                            .get(url = URL.root.port(port))
                            .addHeaders(Headers(Header.Accept(MediaType.text.`plain`)))
      _                <- fixture.defaultTestServer
      helloResponse    <- client.batched(Request.get(testRequest.url / "hello" / "world"))
      helloBody        <- helloResponse.body.asString
      fallbackResponse <- client.batched(Request.get(testRequest.url / "any"))
      fallbackBody     <- fallbackResponse.body.asString
    } yield assertTrue(helloBody == "Hey there!", fallbackBody == "fallback")
  }

  lazy val test2: Spec[MyHttpServer & TestServer & Client, Throwable] = test("test hello world route") {
    val fixture = new TestConfiguration
    for {
      client             <- ZIO.service[Client]
      port               <- ZIO.serviceWithZIO[Server](_.port)
      testRequest         = Request
                              .get(url = URL.root.port(port))
                              .addHeaders(Headers(Header.Accept(MediaType.text.`plain`)))
      _                  <- fixture.createTestServerEffect
      helloWorldResponse <- client.batched(Request.get(testRequest.url))
      helloWorld         <- helloWorldResponse.body.asString
    } yield assertTrue(helloWorld == "Hello World!")
  }

  lazy val test3: Spec[MyHttpServer & TestServer & Client, Throwable] = test("test json hello world route") {
    val fixture = new TestConfiguration
    for {
      client             <- ZIO.service[Client]
      port               <- ZIO.serviceWithZIO[Server](_.port)
      testRequest         = Request
                              .get(url = URL.root.port(port))
                              .addHeaders(Headers(Header.Accept(MediaType.application.json)))
      _                  <- fixture.createTestServerEffect
      helloWorldResponse <- client.batched(Request.get(testRequest.url / "json"))
      helloWorld         <- helloWorldResponse.body.asString
    } yield assertTrue(helloWorld == """{"greetings": "Hello World!"}""")
  }

  lazy val test4: Spec[MyHttpServer & TestServer & Client, Throwable] = test("test json collection route") {
    val fixture = new TestConfiguration
    for {
      client             <- ZIO.service[Client]
      port               <- ZIO.serviceWithZIO[Server](_.port)
      testRequest         = Request
                              .get(url = URL.root.port(port))
                              .addHeaders(Headers(Header.Accept(MediaType.application.json)))
      _                  <- fixture.createTestServerEffect
      booksArrayResponse <- client.batched(Request.get(testRequest.url / "books"))
      responseStr        <- booksArrayResponse.body.asString
      myJsonBooks        <- ZIO.fromEither(io.circe.parser.decode[Vector[Book]](responseStr))
    } yield assertTrue(myJsonBooks.size == 7)
  }

  val listOfTests: List[Spec[Any, Throwable]] = List(test1, test2, test3, test4).map(
    _.provide(
      Client.default,
      ZLayer.succeed(Server.Config.default.onAnyOpenPort),
      TestServer.layer,
      ZLayer.succeed(NettyConfig.defaultWithFastShutdown),
      NettyDriver.customized,
      MyHttpServer.testConfig,
      BookRepository.testConfig,
    ),
  )

  def spec: Spec[TestEnvironment & Scope, Any] = suite("Test http App")(
    listOfTests*,
  )
}

class TestConfiguration {

  val testRoutes: Routes[Any, Nothing]          = Routes(
    Method.GET / trailing          -> handler {
      Response.text("fallback")
    },
    Method.GET / "hello" / "world" -> handler {
      Response.text("Hey there!")
    },
  )
  val defaultTestServer: URIO[TestServer, Unit] = TestServer.addRoutes(testRoutes)

  lazy val createTestServerEffect: URIO[MyHttpServer & TestServer, Unit] =
    ZIO
      .service[MyHttpServer]
      .flatMap(myTestServer => TestServer.addRoutes(myTestServer.routes.myRoutes))
}
