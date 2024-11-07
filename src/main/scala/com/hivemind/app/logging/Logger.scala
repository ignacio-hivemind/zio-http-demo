package com.hivemind.app.logging

import zio.{Console, Ref, Scope, UIO, ULayer, ZIO, ZLayer}

trait Logger {
  def log(message: String): UIO[Unit]
}

object Logger {
  val live: ULayer[Logger] = ZLayer.succeed(LoggerImpl())
}
