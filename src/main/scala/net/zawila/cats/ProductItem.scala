package net.zawila.cats

sealed trait ProductItem

case class Book(pages: Int) extends ProductItem
case class NotePad() extends ProductItem
