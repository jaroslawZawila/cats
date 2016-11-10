package net.zawila.cats

trait Weightable {
  def weight: Int
}

trait Weight[T] {
  def weight(t: T): Weightable
}

object WeightCalculator {
  def calculate(items: List[Weightable]): Int = items.foldLeft(0)(_ + _.weight)
}

object WeightableProducts {

  implicit val book2Weight = new Weight[Book] {
    override def weight(book: Book): Weightable = new Weightable {
      override def weight: Int = book.pages / 10
    }}

  implicit val notepad2Weight = new Weight[NotePad] {
    override def weight(notepad: NotePad): Weightable = new Weightable {
      override def weight: Int = 10
    }}
}

object Main{

  def main (args: Array[String] ): Unit = {
    val fruits = List(new Apple, new Apple, new Grape, new Banana)
    val fruitsWeight = WeightCalculator.calculate(fruits)

    println(s"Fruits weight: ${fruitsWeight}")

    import WeightableProducts._

    implicit def listToWeightableList(products: List[Product]): List[Weightable] = {
      products.map( x =>  x match {
        case n: NotePad => implicitly[Weight[NotePad]].weight(n)
        case b: Book => implicitly[Weight[Book]].weight(b)
        case w: Weightable => w
      })
    }

    val products = List(new Book(350), new NotePad, new NotePad)
    val productsWeight = WeightCalculator.calculate(products)

    println(s"Products weight: ${productsWeight}")

//    val mixedList = List(new Book(350), new Apple)
//    val mixedWeight = WeightCalculator.calculate(mixedList)
//
//    println(s"Mixed weight: ${mixedWeight}")
  }
}
