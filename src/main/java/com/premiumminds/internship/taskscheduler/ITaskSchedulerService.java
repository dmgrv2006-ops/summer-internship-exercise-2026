package com.premiumminds.internship.taskscheduler;

import java.util.Collection;
import java.util.List;

public interface ITaskSchedulerService {
    /**
     * Returns all tasks that are currently eligible for execution.
     * A task is eligible if its status is PENDING and all its dependencies are COMPLETED.
     * Circular dependencies should not be checked here.
     *
     * @param tasks the collection of tasks
     * @return eligible tasks ordered by priority (lower value = higher priority)
     */
    List<Task> getEligibleTasks(Collection<Task> tasks);

    /**
     * Returns the full execution order for all tasks, respecting dependencies and priority.
     *
     * @param tasks the collection of tasks
     * @return tasks ordered by valid execution sequence
     * @throws IllegalArgumentException if a circular dependency or missing dependency is detected
     */
    List<Task> getExecutionOrder(Collection<Task> tasks);
}
