package karya.data.psql.tables.tasks

import karya.data.psql.tables.jobs.JobsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object TasksTable : Table("tasks") {
	val id = uuid("id")
	val jobId = uuid("job_id") references JobsTable.id
	val partitionKey = integer("partition_key")
	val status = integer("status")
	val createdAt = timestamp("created_at")
	val executedAt = timestamp("executed_at").nullable()
	val nextExecutionAt = timestamp("next_execution_at").nullable()

	override val primaryKey = PrimaryKey(id, partitionKey)
}
