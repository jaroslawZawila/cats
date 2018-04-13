package net.zawila.cats

import cats.data.EitherT
import cats.implicits._

case class ArrangementServiceError(error: String)

case class DynamoClientError(error: String)

case class Response(message: String)

object EitherTExample extends App {

  type ArrangementResponse[T] = Option[Either[ArrangementServiceError, T]]
  type DynamoClientResponse[T] = Option[Either[DynamoClientError, T]]

  def getArrangemnt: ArrangementResponse[Boolean] = Some(Left(ArrangementServiceError("Error AS")))
  def getItem: DynamoClientResponse[Int] = Some(Left(DynamoClientError("Error Dynamo")))

  def getArrangemntT: ArrangementResponse[Boolean] = Some(Right(true))
  def getItemT: DynamoClientResponse[Int] = Some(Right(200))


  implicit def arrangemenErrorToHttp(e: ArrangementServiceError) = Response(e.error)
  implicit def dynamoClientErrorToHttp(e: DynamoClientError) = Response(e.error)

  val failed = for {
    a <- call(getArrangemnt)
    b <- call(getItem)
  } yield Response(b.toString)

  val success = for {
    a <- call(getArrangemntT)
    b <- call(getItemT)
  } yield Response(b.toString)

  val z: Option[Response] = failed.merge
  val zz: Option[Response] = success.merge

  println(failed.merge)
  println(success.merge)

  def call[T, R](s: Option[Either[T, R]])(implicit f: T => Response): EitherT[Option, Response, R] = EitherT(s).leftMap(f)
}


