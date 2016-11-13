package net.zawila.kafka

import org.apache.kafka.clients.producer.RecordMetadata

import java.util.concurrent.Future

object Kafka extends App {

  val topic = "test-topic-1"
  println(s"Connecting to ${topic}")

  import org.apache.kafka.clients.producer.KafkaProducer

  val properties = new java.util.Properties()
  properties.put("bootstrap.servers", "localhost:9092")
  properties.put("client.id", "KafkaProducer-01")
  properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer")
  properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[Integer, String](properties)

  import org.apache.kafka.clients.producer.ProducerRecord

  val polish = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss")
  val now = java.time.LocalDateTime.now().format(polish)
  val record = new ProducerRecord[Integer, String](topic, 2, s"hello at $now")

  val metaF: Future[RecordMetadata] = producer.send(record)
  val meta = metaF.get() // blocking!
  val msgLog =
  s"""
     |offset    = ${meta.offset()}
     |partition = ${meta.partition()}
     |topic     = ${meta.topic()}
     """.stripMargin
  println(msgLog)

  producer.close()

}
