CREATE TABLE IF NOT EXISTS tasks (
    id UUID,
    plan_id UUID REFERENCES plans(id),
    partition_key INT NOT NULL,
    status INT,
    created_at TIMESTAMP,
    executed_at TIMESTAMP NULL,
    next_execution_at TIMESTAMP NULL,

    PRIMARY KEY (id, partition_key)
) PARTITION BY HASH (partition_key);

CREATE INDEX idx_tasks_partition_key_status_next_execution_at
ON tasks(partition_key, status, next_execution_at);
