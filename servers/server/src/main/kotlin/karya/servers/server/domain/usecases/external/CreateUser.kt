package karya.servers.server.domain.usecases.external

import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.repos.UsersRepo
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

/**
 * Use case for creating a new user.
 *
 * @property usersRepo The repository for accessing users.
 */
class CreateUser
@Inject
constructor(
  private val usersRepo: UsersRepo,
) {
  companion object : Logging

  /**
   * Invokes the create user use case.
   *
   * @param request The request to create a new user.
   * @return The created user.
   */
  suspend fun invoke(request: CreateUserRequest): User =
    User(request)
      .also { usersRepo.add(it) }
      .also { logger.info("[USER_CREATED] --- $it") }
}
