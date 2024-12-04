package karya.core.actors

import karya.core.entities.Action
import karya.core.entities.ExecutorResult
import java.util.*

/**
 * Interface representing a connector that can invoke actions and handle shutdowns.
 *
 * @param T The type of action that the connector can handle.
 */
interface Connector<T : Action> {

  /**
   * Invokes the specified action for the given plan ID.
   *
   * @param planId The unique identifier of the plan.
   * @param action The action to be invoked.
   * @return The result of the executor's operation.
   */
  suspend fun invoke(planId: UUID, action: T): ExecutorResult

  /**
   * Shuts down the connector.
   */
  suspend fun shutdown()
}
