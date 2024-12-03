# Hooks

One can attach hooks to the life cycle state of tasks to:

- Get updates
- Create notifications
- Trigger another task

This empowers Karya to link and connect more tasks and not just bind itself to the life cycle of the one that you have scheduled.

## Supported state transition hooks

Currently, one can configure hooks on the following state transitions:

| Transition | Subscription                                                  |
| ---------- |---------------------------------------------------------------|
| `ON_FAILURE` | Subscribe to when/if any task of the plan you submitted fails |
| `ON_COMPLETION` | Subscribe to when your plan has finished all its tasks. This is applicable when the plan is either `ONE_TIME` or bounded `RECURRING` |

## Configuring Hooks

Karya is built using the philosophy - *Eat what you kill*

What this means is that the same interface that you used to configure actions while submitting the plan can be used to configure hooks as well!
This has the advantage that:

1. **Ease of use** - `Action` class that was used to describe what execution to perform while creating the plan can also be used here.
2. **More configuration options** - One is limited only by the number of connectors configured to set up various actions on hook!
3. **Extending Lifecycle** - Using the special `Action.ChainedPlan`, one can technically 'extend' the lifecycle of the plan they submitted.

> **NOTE:** When chaining a plan to lifecycle, user will have to monitor the lifecycle of the chained tasks themselves. i.e. unlike in explicit ChainedPlan where on cancelling the parent plan will cancel all of its child plans, tasks triggered as a result of Hook action will not keep a track of such lineage.

Sample [MakeDelayApiCallWithCompletionHook.kt](../../docs/samples/src/main/kotlin/karya/docs/samples/MakeDelayApiCallWithCompletionHook.kt) request
