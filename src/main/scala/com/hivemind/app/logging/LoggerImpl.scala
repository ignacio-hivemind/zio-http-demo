package com.hivemind.app.logging

import zio.{Console, UIO}

case class LoggerImpl() extends Logger {
  override def log(message: String): UIO[Unit] =
    Console.printLine(message).ignore.unit
}
