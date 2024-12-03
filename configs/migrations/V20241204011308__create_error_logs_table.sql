CREATE TABLE IF NOT EXISTS error_logs (
    job_id UUID REFERENCES jobs(id),
    error VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    task_id UUID,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_jobs_id ON error_logs(id);
