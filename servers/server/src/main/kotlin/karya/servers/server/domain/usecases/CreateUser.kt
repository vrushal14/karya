package karya.servers.server.domain.usecases

import karya.core.entities.User
import karya.core.entities.requests.CreateUserRequest
import karya.core.repos.UsersRepo
import org.apache.logging.log4j.kotlin.Logging
import org.apache.logging.log4j.kotlin.logger
import javax.inject.Inject

class CreateUser
@Inject
constructor(
  private val usersRepo: UsersRepo,
) {
  companion object : Logging

  suspend fun invoke(request: CreateUserRequest): User =
    User(request)
      .also { usersRepo.add(it) }
      .also { logger.info("[USER_CREATED] --- $it") }
}
