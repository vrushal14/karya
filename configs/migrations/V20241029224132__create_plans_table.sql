CREATE TABLE IF NOT EXISTS plans (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    description VARCHAR(255) NOT NULL,
    period_time VARCHAR(255) NOT NULL,
    type JSON NOT NULL,
    status INT NOT NULL,
    max_failure_retry INT NOT NULL,
    action JSON NOT NULL,
    hook JSON NOT NULL,
    parent_plan_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_plan_id ON plans(id);
