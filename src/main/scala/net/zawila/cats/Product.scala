package net.zawila.cats

sealed trait Product

case class Book(pages: Int) extends Product
case class NotePad() extends Product
