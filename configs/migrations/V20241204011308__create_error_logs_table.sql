CREATE TABLE IF NOT EXISTS error_logs (
    plan_id UUID REFERENCES plans(id),
    error VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    task_id UUID,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_error_logs_plan_id ON error_logs(plan_id);
