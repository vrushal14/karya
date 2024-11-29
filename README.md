# Karya

Distributed, scalable Job Scheduler built for high throughput.

- [How to contribute](.github/CONTRIBUTING.md)

---

## Usecases

1. Schedule jobs to run at periodic frequency.
2. Schedule async, non-recurring jobs to run at a particular time in the future.
3. Chain recurring and non-recurring jobs together to achive custom flow.

 ## Why Karya?

 There are several job schedulers out there. Why to chose Karya? Here are the reasons:

 1. Karya is **built for high throughput**. It follows the philosphy: *solve scaling by throwing money at it*, which means one just needs to add one more node and scale horizontally infinitly!
 2. Software like Postgres and Redis have already solved the problem of achieving fault tolerancy. Karya nodes are stateless in nature and utilizes the properties of such components to **achieve fault tolerancy**, thus making it **less complex and easy to understand**!
 3. Karya is **highly pluggable** in nature. Be it in terms of [data-interfaces](#data-interfaces) or [connectors](#connectors). Just specify the properties in a .yml file and you're good to go!
 4. Being written in Kotlin, it smartly uses coroutines to achieve *structured concurrency* making it **thread safe while being blazingly fast and performant**!

## Overview

![overview.png](./docs/media/overiew.png)

### Components

Karya has the following components which helps it achieve its functionality. Note that **all of the components are stateless** hence can be scaled in numbers according to your requirements without any issue.

#### Client

Users can integrate the Karya client into their services to:

- Create a user
- Use that user to schedule jobs
- Update/cancel jobs

#### Server

This is a web server via which the client interacts. It has the following functions:

1. Manage users. Only a registered user can schedule a job.
2. Pushes the job to the repo connector from which the scheduler will poll from

This is a configurable component as Karya will look for an environment variable `KARYA_SCERVER_CONFIG_PATH` to find the .yml file that the scheduler instance will look for at runtime. The structure of the yml file is as follows:

   | Server.yml Key | Sample Value      | Description      |
|-------------|-------------|------------|
| *application.strictMode* | true| This key if set *false* will allow [chained jobs](#chained-jobs) to be recurring in nature. Note that this can lead to number of jobs being scheduled exponentially, so **proceed with caution**. |
| *application.chainedDepth* | 2 | [Chained jobs](#chained-jobs) are nothing but recursive triggers. As such, one can specify till what depth can this chain be, by setting this variable |

[Sample server.yml file](.configs/server.yml)

#### Scheduler

- This is the heart of Karya. it will constantly keep polling the repo, and when it is time to execute the task, will it push to the queue.
- This is the component which has the logic as to when and if the next task is to be scheduled.

This is a configurable component as Karya will look for an environment variable `KARYA_SCHEDULER_CONFIG_PATH` to find the .yml file that the scheduler instance will look for at runtime. The structure of the yml file is as follows:

| Scheduler.yml Key        | Sample Value       | Sescription       |
|-------------|-------------|-------------|
| *application.threadCount* | 2      | How many threads you want one instance of scheduler to use     |
| *application.workers* | 3      | How many workers within the instance do you want to spin up. Useful when the throughput is high     |
| *application.fetcher.channelCapacity* | 10      | Executor *polls* for messages from the repo and pushes it to a local queue from which the workers consume from. This property defines the size of this queue explicitly to prevent a *OutOfMemory* scenario.     |
| *application.fetcher.pollFrequency* | 250      | Polling frequency from the repo, lesser the value, better the precision of when the task should be executed |
| *application.fetcher.executionBuffer* | 1000      | This specifies how far back from the time of polling should scheduler look for an unexecuted task. This should always be higher than pollFrequency |
| *application.fetcher.repoPartitions* | List<Int>      | From which partitions of the repo should the scheduler instance should poll from. |

[Sample scheduler.yml file](.configs/scheduler.yml)

#### Executor

- Executors receive a task from the worker-queue and does the heavy work that the user specifies.
- User can specify what action should the executor perform once it receives the task. This is done via setting an environment variable `KARYA_EXECUTOR_CONFIG_PATH` that the executor will look for to initialize the connector-plugins at run time.
- [Connector-Plugins](./README.md/#connector-plugin) define what operations at the time of execution should the executor support.

  ```yml
  application:
   connectors:                    // this here is the list of connector-plugins and their properties that the executor should support
     - type: "restapi"            // in this example, we are configuring the executor to support a task that involves making a REST call when it has to execute
       configs:
         keepAliveTime: 3000
         connectionTimeout: 1000
         connectionAttempts: 3
  ```

  [Sample executor.yml file](.configs/executor.yml)

---

## Data Interfaces

Karya's "distributed" nature comes not olny from it's ability to scale by spinning up more scheduler/executor nodes. But it leverages the distributed nature of other softwares. To run, it needs at least these 3 components:

1. **Repo Interface** : Karya uses a SQL based database for persistent storage of jobs/tasks/users
2. **Locks Interface** : Karya uses a distributed mutex lock to prevent tasks to prevent race conditions
3. **Queue Interface** : Karya buffers the execution of tasks in queues.

**NOTE** : Karya is built for high throughput and the tradeoff for it is precision as to when a task should be executed. But the hit in precision can be minimized by the configurability that Karya provides albeit at the cost of more resources.

### Supported Interfaces

Karya provides ability to just plug and play provided the implementation for the said interface is supported by it. Users can easily connect their existing repo/locks/queues with Karya, spin up Karya's nodes and start scheduliing jobs! The following interfaces are currently implmented within Karya with rest more on the way:

| Repo Interface        | Locks Interface       | Queue Interface       |
|-------------|-------------|-------------|
| [Postgres](https://www.postgresql.org/) | [Redis](https://redis.io/)      | [RabbitMQ](https://www.rabbitmq.com/)     |

### How to configure interfaces?

You have your said components running. We must now provide a way for Karya to connect with it. We do this by specifying all the configurations in a .yml file and set the environment variable `KARYA_PROVIDERS_CONFIG_PATH` to point to this file. 

Karya will read the contents of the yml file and connect to your repos/locks/queues! The format of the `providers.yml` file is as follows:

```yml

// repo configurations
repo:
  provider: "psql"
  partitions: 5  // how many partitions you would want in your database. Usefull when the throughput is high
  properties:
    hikari:
      // can provide any Hikari datasource properties here
    flyway:
      url: "jdbc:postgresql://localhost:5432/karya"
      user: "karya"
      password: "karya"

// lock configurations
lock:
  provider: "redis"
  properties:
    hostname: "localhost"
    port: 6379
    waitTime: 1000
    leaseTime: 5000

// queue configurations
queue:
  provider: "rabbitmq"
  properties:
    username: "karya"
    password: "karya"
    virtualHost: "/"
    hostname: "localhost"
    port: 5672
```

 [Sample providers.yml file](./configs/providers.yml)

The above .yml file describes a way where we are using *Postgres as repo*, *Redis as Locks* and *RabbitMQ as queue*. But you can connect any interface of your choice and provided Karya has the ability to support it, it should work seamlessly. 

To swap in a different interfaces, change the `provider` key in any of repo/lock/queue section and pass in the properties accordingly. The mapping of interface to provider key is as follows:

| Implementation       | Interface      | Provider key       |
|-------------|-------------|-------------|
| Postgres | repo      | *psql*     |
| Redis | locks      | *redis*     |
| RabbitMQ | queue      | *rabbitmq*     |

---

## Connectors

This here is the section which defines what all actions does Karya supports when a task has to be executed by the executor.
This has to be set in the executor.yml file before starting the executor. Currently, the following connector plugins are available:

| Executor.yml key      | Description      |
|-------------|-------------|
| *restapi* | This plugin helps support the executor make a REST Api call |

[Sample MakePeriodicRestApiCall.kt example](./docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicApiCall.kt).

### Default Connector Plugins

Some plugins need not be specified in the `executor.yml` file as they are natively supported by the Executor. They are:

#### Chained Jobs

Sometimes, one would have a usecase where they would want to schedule a periodic job, *starting* from a definite point in the future. This could be achieved by scheduling a one time job to trigger at the that point in time in the future. And that trigger would be to schedule the periodic job that you want to run. We call this **chained jobs**.

Karya supports chained jobs, albeit with some configuration which needs to be specified in the `server.yml`.

[Sample MakeChainedKaryaCall.kt.kt example](./docs/samples/src/main/kotlin/karya/docs/samples/MakeChainedKaryaCall.kt.kt).
