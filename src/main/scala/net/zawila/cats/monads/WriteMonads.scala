package net.zawila.cats.monads

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import cats.data.Writer
import cats.implicits._


object WriteMonads {

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body:  => Logged[A]) = {
    try body finally Thread.sleep(100)
  }

  def factorial(n: Int): Logged[Int] = {
    import cats.syntax.applicative._

    if(n==0) {
        1.pure[Logged]
      } else {
        for {
          a <- slowly(factorial(n - 1))
          _ <- Vector(s"fact ${n} ${a * n}").tell
        } yield  a * n
      }
  }


  def main(args: Array[String]): Unit = {

    val result = Await.result(Future.sequence(Vector(
      Future(factorial(5)),
      Future(factorial(5))
    )), Duration.Inf)

    println(s"Result: ${result}")

  }


}

