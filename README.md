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

## Linting and Formatting

- Detekt Plugin is being used to enforce code style and formatting
- This is part of the build step hence ensure `./gradlew detekt` runs successfully for the build to succeed.
- Ruleset can be found [here](./detekt.yml)

### Intellij Config

Additionally in Intellij, configure the following:

Set the indentation to space : 2
[indentation_settings](./docs/media/intellij_indentation.png)

While running the Intellij Formatter, set `optimise import` to true
[format_settings]("./docs/media/intellij_format.png")