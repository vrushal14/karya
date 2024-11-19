# Karya
Distributed, scalable Job Scheduler

![overview.png](./docs/media/overiew.png)

## Setup

```bash
docker run --name postgres-karya -e POSTGRES_USER=karya -e POSTGRES_PASSWORD=karya -e POSTGRES_DB=karya -p 5432:5432 -d postgres
```