package karya.connectors.kafka.di

import dagger.BindsInstance
import dagger.Component
import karya.connectors.kafka.configs.KafkaConnectorConfig
import karya.core.actors.Connector
import karya.core.entities.Action
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    KafkaConnectorModule::class
  ]
)
interface KafkaConnectorComponent {

  val connector: Connector<Action.KafkaProducerRequest>

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun kafkaConnectorConfig(config: KafkaConnectorConfig): Builder

    fun build(): KafkaConnectorComponent
  }
}
