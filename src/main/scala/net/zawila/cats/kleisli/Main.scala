package net.zawila.cats.kleisli

import cats.arrow.FunctionK
import cats.data.{Cokleisli, Kleisli}
import cats.implicits._

import scala.concurrent.Future

final case class Error(value: String)

object Example {

  def parse: Kleisli[Option, String, Int] = Kleisli(x => Some(x.toInt))

  def divide: Kleisli[Option, Int, Double] = Kleisli(x => Some(1.0 / x))

  def parseAndDivide = (parse andThen divide).run

  def local = parse.local[Error](e => e.value)

  val x: Cokleisli[Option, Int, String] = Cokleisli(a => a.get.toString)

  val e: Either[Int, String] = Right("1")

  def e1: Kleisli[Future, String, Int] = Kleisli(x => Future.successful(x.toInt))

  val fk = new FunctionK[Future, Option] {
    def apply[A] (f: Future[A]): Option[A] = f.value.get.toOption
  }

  def e2 = e1.transform(fk) andThen divide
}


object Main extends App {

  import Example._

  val r = parseAndDivide("5")
  val f = local(Error("5"))

  println(r)
  println(f)

  val r1 = x.run(Some(1))

  println(r1)

  println(e2("5"))
}
