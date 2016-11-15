package net.zawila.kafka

import org.apache.kafka.clients.producer.RecordMetadata
import java.util.concurrent.Future

import collection.JavaConverters._
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

object Kafka extends App {

  val topic = "test-topic-2"
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

  val propertiesConsumer = new java.util.Properties()
  propertiesConsumer.put("bootstrap.servers", "localhost:9092")
  propertiesConsumer.put("group.id", "mygroup")
  propertiesConsumer.put("auto.offset.reset", "earliest")
  propertiesConsumer.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer")
  propertiesConsumer.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer[Integer, String](propertiesConsumer)

  println(1)
  consumer.subscribe(List(topic).asJavaCollection)

  println(2)
  val msg: ConsumerRecords[Integer, String] = consumer.poll(10)

  println(3)
  msg.iterator().asScala.foreach(
    x => println( s"Message body:  ${x.value()}")
  )



}
