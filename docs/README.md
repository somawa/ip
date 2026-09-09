# Blud User Guide

Blud manages todo, deadline, and event tasks through commands.

## Sorting tasks

Use `sort` to reorder tasks in the current session. Sorting does not write the
new order to the storage file by itself.

```text
sort deadline [asc|desc]
sort event [asc|desc]
sort status [asc|desc]
```

The direction defaults to ascending. `sort deadline` orders deadline tasks
from earliest to latest, with tasks without deadlines first. `sort event`
orders events by their start date, and `sort status` places incomplete tasks
before completed tasks. Use `desc` to reverse the order of values.

Sorting is stable, so tasks with equal values retain their relative order.
The current order is used by `list`, `find`, `mark`, `unmark`, and `delete`.
New tasks are inserted according to the most recent sort during the current
session.

Examples:

```text
sort deadline
Tasks sorted by deadline in ascending order.

sort status desc
Tasks sorted by completion status in descending order.
```
