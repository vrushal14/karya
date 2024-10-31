package karya.data.psql.repos

import karya.core.entities.User
import karya.core.repos.UsersRepo
import karya.data.psql.tables.users.UsersQueries
import java.util.*
import javax.inject.Inject

class PsqlUsersRepo
@Inject
constructor(
  private val usersQueries: UsersQueries
) : UsersRepo {

  override suspend fun add(user: User) {
    usersQueries.add(user)
  }

  override suspend fun get(id: UUID): User? =
    usersQueries.get(id)
}