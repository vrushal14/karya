# Data Interfaces

Karya's *distributed* nature comes not only from its ability to scale by spinning up more scheduler/executor nodes. But
it leverages the distributed nature of other pieces of software. To run, it needs at least these 3 components:

1. **Repo Interface** : A SQL based database for persistent storage
2. **Locks Interface** : A distributed mutex lock to prevent race conditions
3. **Queue Interface** : To buffers the execution of tasks.

> **NOTE** : Karya is built for high throughput and the tradeoff for it is precision as to when a task should be
> executed. But the hit in precision can be minimized by the configurability that Karya provides albeit at the cost of
> more resources.

---

## Supported Interfaces

Karya provides ability to just plug and play provided the implementation for the said interface is supported by it.
Users can easily connect their existing repo/locks/queues with Karya, spin up Karya's nodes and start scheduling tasks!
The following interfaces are currently implemented within Karya with rest more on the way:

| Repo Interface                          | Locks Interface            | Queue Interface                       |
|-----------------------------------------|----------------------------|---------------------------------------|
| [Postgres](https://www.postgresql.org/) | [Redis](https://redis.io/) | [RabbitMQ](https://www.rabbitmq.com/) |

---

## How to configure interfaces?

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
properties accordingly. The mapping of interface to provider key is as follows:

| Implementation | Interface | Provider key |
|----------------|-----------|--------------|
| Postgres       | repo      | *psql*       |
| Redis          | locks     | *redis*      |
| RabbitMQ       | queue     | *rabbitmq*   |

---
