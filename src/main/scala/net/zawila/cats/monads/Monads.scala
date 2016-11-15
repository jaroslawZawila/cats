package net.zawila.cats.monads

import cats._
import cats.implicits._

sealed trait Result[+A]

final case class Success[A](value: A) extends Result[A]
final case class Warning[A](value: A, msg: String) extends Result[A]
final case class Failure(msg: String) extends Result[Nothing]

object Monads {

  implicit val resultMonad = new Monad[Result] {

    override def pure[A](x: A): Result[A] = Success(x)

    override def flatMap[A, B](fa: Result[A])(f: (A) => Result[B]): Result[B] = fa match {
      case Success(v) => f(v)
      case Warning(v,m) => f(v) match {
        case Success(v) => Warning(v,m)
        case Warning(v,m2) => Warning(v, s"${m}, ${m2}")
        case Failure(m) => Failure(m)

      }
      case Failure(m) => Failure(m)

    }

    override def tailRecM[A, B](a: A)(f: (A) => Result[Either[A, B]]): Result[B] = ???

  }

  def success[A](v: A): Result[A] = Success(v)
  def warning[A](v: A, m: String): Result[A] = Warning(v,m)
  def failure[A](m: String): Result[A] = Failure(m)

  def main(args: Array[String]): Unit = {
    val r: Result[Int] = warning(100, "message1") flatMap (x => Warning(x*3, "Message 2"))

    val r1: Result[Int] = warning(3, "some").map(x => {println(x); x * 101})

    println(r1)

    println(r)


  }
}

