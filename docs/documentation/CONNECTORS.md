# Connectors

This here is the section which defines what all actions do Karya supports when a task has to be executed by the
executor.
This has to be set in the executor.yml file before starting the executor. Currently, the following connector plugins are
available:

| Executor.yml key | Description                                                 |
|------------------|-------------------------------------------------------------|
| *restapi*        | This plugin helps support the executor make a REST Api call |

[Sample MakePeriodicRestApiCall.kt example](../../docs/samples/src/main/kotlin/karya/docs/samples/MakePeriodicApiCall.kt).

---

## Default Connector Plugins

Some plugins need not be specified in the `executor.yml` file as they are natively supported by the Executor. They are:

### Chained Plans

At times, one would have a use case where they would want to schedule a periodic task, *starting* from a definite point
in the future. This could be achieved by scheduling a one time task to trigger at the that point in time in the future.
And that trigger would be to schedule the periodic task that you want to run. We call this **chained plans**.

> Karya will monitor the lifecycle of chained plans, i.e. on cancelling the parent plan, Karya will automatically cancel all the child/grandchild plans that got triggered!

Karya supports chained plans, albeit with some configuration which needs to be specified in the [server.yml](../documentation/COMPONENTS.md/#server).

[Sample MakeChainedKaryaCall.kt.kt example](./docs/samples/src/main/kotlin/karya/docs/samples/MakeChainedKaryaCall.kt.kt).
