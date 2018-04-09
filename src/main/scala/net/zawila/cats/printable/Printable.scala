package net.zawila.cats.printable

import cats._
import cats.implicits._


trait Printable[A] {
  def format(a: A): String
}

object PrintDefaults {

  implicit  val stringToPrintable = new Printable[String] {
    override def format(a: String): String = a
  }

  implicit  val intToPrintable = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }

  implicit val cat2Printable = new Printable[Cats] {
    override def format(a: Cats): String = s"${a.name} is a ${a.age} years old ${a.color} cat."
  }
}

object Print {
  def format[A](a: A)(implicit p: Printable[A]): String = {
    p.format(a)
  }

  def print[A](a: A)(implicit p: Printable[A]): Unit = {
    println(p.format(a))
  }
}

object PrintSyntax {
  implicit class PrintOps[A](a: A) {
    def format(implicit p: Printable[A]) = Print.format(a)

    def print(implicit p: Printable[A]): Unit = Print.print(a)
  }
}

object ShowSyntax {
  implicit val catShow = Show.show[Cats]{
    cat => s"${cat.name} is a ${cat.age} years old ${cat.color} cat."
  }
}

case class Cats(name: String, age: Int, color: String)

object Main{

  def main(args: Array[String]): Unit = {
    val s = "Something"
    val cat = new Cats("Borys", 12, "brown")

    import PrintDefaults._
    import PrintSyntax._
    Print.print(s)
    Print.print(1)
    Print.print(cat)

    cat.format
    cat.print

    val show = Show.apply[Int]
    val intAsString = show.show(2)

    import cats.syntax.show._
    println(1.show)

    import ShowSyntax._
    println(cat.show)

  }

}
