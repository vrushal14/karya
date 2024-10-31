CREATE TABLE IF NOT EXISTS jobs (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    period_time VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    status INT NOT NULL,
    max_failure_retry INT NOT NULL,
    executor_endpoint VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_jobs_id ON jobs(id);