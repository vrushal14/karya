package karya.data.psql.tables.users

import karya.core.entities.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class UsersQueries
@Inject
constructor(
  private val db : Database
) {

  fun add(user: User) = transaction(db) {
    UsersTable.insert {
      it[id] = user.id
      it[name] = user.name
      it[createdAt] = Instant.ofEpochMilli(user.createdAt)
    }
  }

  fun get(id : UUID) = transaction(db) {
    UsersTable.selectAll()
      .where { UsersTable.id eq id }
      .firstOrNull()
  }?.let { fromRecord(it) }

  private fun fromRecord(resultRow: ResultRow) = User(
    id = resultRow[UsersTable.id],
    name = resultRow[UsersTable.name],
    createdAt = resultRow[UsersTable.createdAt].toEpochMilli()
  )
}