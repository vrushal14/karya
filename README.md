# Karya
Distributed, scalable Job Scheduler

![overview.png](./docs/media/overiew.png)

## Setup

```bash
// setup postgres
docker run --name postgres-karya -e POSTGRES_USER=karya -e POSTGRES_PASSWORD=karya -e POSTGRES_DB=karya -p 5432:5432 -d postgres

// setup RabbitMQ
docker run -d --name rabbitmq-karya -e RABBITMQ_DEFAULT_USER=karya -e RABBITMQ_DEFAULT_PASS=karya -e RABBITMQ_DEFAULT_VHOST=/ -p 5672:5672 -p 15672:15672 rabbitmq:management
```