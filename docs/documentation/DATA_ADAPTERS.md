# Data Adapters

The ability of Karya to _adapt_ to different data sources is what makes it a powerful tool. Karya provides various adapters so that users can integrate their existing data sources with Karya and start scheduling tasks.

Karya requires at least these 3 interfaces to be implemented for it to work:

1. **Repo** : A SQL based database for persistent storage
2. **Locks** : A distributed mutex lock to prevent race conditions
3. **Queue** : To buffers the execution of tasks.

> **NOTE** : Karya is built for high throughput and the tradeoff for it is precision as to when a task should be
> executed. But the hit in precision can be minimized by the configurability that Karya provides albeit at the cost of
> more resources.

---

## Supported Adapters

Karya provides ability to just plug and play provided the implementation for the said interface is supported by it.
Users can easily connect their existing repo/locks/queues with Karya, spin up Karya's nodes and start scheduling tasks!
The following interfaces are currently implemented within Karya with rest more on the way:

| Repo Adapter                            | Locks Adapter              | Queue Adapter                         |
|-----------------------------------------|----------------------------|---------------------------------------|
| [Postgres](https://www.postgresql.org/) | [Redis](https://redis.io/) | [RabbitMQ](https://www.rabbitmq.com/) |

---

## How to configure adapters?

You have your said components running. We must now provide a way for Karya to connect with it. We do this by specifying
all the configurations in a .yml file and set the environment variable `KARYA_PROVIDERS_CONFIG_PATH` to point to this
file.

Karya will read the contents of the yml file and connect to your repos/locks/queues! The format of the `providers.yml`
file is as follows:

```yml
repo:
  provider: "psql"
  partitions: 5  // how many partitions you would want in your database. Usefull when the throughput is high
  properties:
    // Properties of the repo provider

lock:
  provider: "redis"
  properties:
    // Properties of the locks provider

queue:
  provider: "rabbitmq"
  properties:
    // Properties of the queue provider
```

[Sample providers.yml file](../../configs/providers.yml)

The above sample .yml file describes a way where we are using *Postgres as repo*, *Redis as Locks* and *RabbitMQ as
queue*. But you can connect any interface of your choice and provided Karya has the ability to support it, it should
work seamlessly.

To swap in a different interfaces, change the `provider` key in any of repo/lock/queue section and pass in the
properties accordingly.

---

## Configuring Repo Adapter

This section describes the various repo interfaces that can be configured on Karya

### Configuring Postgres

> **providers.yml key:** *psql*

These are the properties that can/should be set for the Postgres repo interface:

| Key      | Description                                                                                                   |
|----------|---------------------------------------------------------------------------------------------------------------|
| *hikari* | One can set all the configurable options provided by hikari here just as you set them in a *.properties* file |
| *flyway.url* | The URL to the flyway migration scripts. This is used to migrate the database schema.                         |
| *flyway.user* | The user to connect to the flyway migration scripts.                                                          |
| *flyway.password* | The password to connect to the flyway migration scripts.                                                    |

<details>
<summary><strong>Example</strong></summary>

```yml
repo:
  provider: "psql"
  partitions: 5
  properties:
    hikari:
      dataSourceClassName: "org.postgresql.ds.PGSimpleDataSource"
      dataSource.user: "karya"
      dataSource.password: "karya"
      dataSource.databaseName: "karya"
      dataSource.portNumber: 5432
      dataSource.serverName: "localhost"
      maximumPoolSize: 1
      connectionTimeout: 5000
    flyway:
      url: "jdbc:postgresql://localhost:5432/karya"
      user: "karya"
      password: "karya"
```

</details>

---

## Configuring Locks Adapter

This section describes the various locks interfaces that can be configured on Karya

### Configuring Redis

> **providers.yml key:** *redis*

These are the properties that can/should be set for the Redis locks interface:

| Key        | Description                                                              |
|------------|--------------------------------------------------------------------------|
| *hostName* | The host where the Redis server is running                               |
| *port*     | The port on which the Redis server is running                            |
| *waitTime* | Set the wait time for which redisson should wait before releasing a lock |
| *leaseTime*| Set the lease time for which the lock should be held                     |

<details>
<summary><strong>Example</strong></summary>

```yml
lock:
  provider: "redis"
  properties:
    hostname: "localhost"
    port: 6379
    waitTime: 1000
    leaseTime: 5000
```

</details>

---

## Configuring Queue Adapter

This section describes the various queue interfaces that can be configured on Karya

### Configuring RabbitMQ

> **providers.yml key:** *rabbitmq*

These are the properties that can/should be set for the RabbitMQ queue interface:

| Key        | Description                                                                 |
|------------|-----------------------------------------------------------------------------|
| *hostName* | The host where the RabbitMQ server is running                               |
| *port*     | The port on which the RabbitMQ server is running                            |
| *username* | The username to connect to the RabbitMQ server                               |
| *password* | The password to connect to the RabbitMQ server                               |
| *virtualHost* | The virtual host to connect to the RabbitMQ server                          |

<details>

<summary><strong>Example</strong></summary>

```yml
queue:
  provider: "rabbitmq"
  properties:
    username: "karya"
    password: "karya"
    virtualHost: "/"
    hostname: "localhost"
    port: 5672
```

</details>

