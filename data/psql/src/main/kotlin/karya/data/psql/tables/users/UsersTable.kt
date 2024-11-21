package karya.data.psql.tables.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : Table("users") {
	val id = uuid("id")
	val name = varchar("name", 255)
	val createdAt = timestamp("created_at")

	override val primaryKey = PrimaryKey(id)
}
