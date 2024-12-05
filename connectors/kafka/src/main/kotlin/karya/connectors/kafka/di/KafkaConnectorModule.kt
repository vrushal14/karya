package karya.connectors.kafka.di

import dagger.Module
import dagger.Provides
import karya.connectors.kafka.KafkaConnector
import karya.connectors.kafka.configs.KafkaConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.Properties
import javax.inject.Singleton

@Module
class KafkaConnectorModule {

  @Provides
  @Singleton
  fun provideKafkaConnector(producer: KafkaProducer<String, String>) : Connector<Action.KafkaProducerRequest> =
    KafkaConnector(producer)

  @Provides
  @Singleton
  fun provideKafkaProducer(config: KafkaConnectorConfig) : KafkaProducer<String, String> {
    val props = Properties()
    props["bootstrap.servers"] = config.bootstrapServers
    props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    return KafkaProducer(props)
  }
}
