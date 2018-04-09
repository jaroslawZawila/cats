package net.zawila.cats

sealed trait Fruit extends Weightable
case class Apple() extends Fruit {
  override def weight: Int = 10
}
case class Grape() extends Fruit {
  override def weight: Int = 5
}
case class Banana() extends Fruit {
  override def weight: Int = 20
}