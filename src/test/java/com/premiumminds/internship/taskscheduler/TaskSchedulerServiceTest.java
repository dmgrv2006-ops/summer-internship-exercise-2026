package com.premiumminds.internship.taskscheduler;

import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskSchedulerServiceTest {

    private ITaskSchedulerService service;

    @BeforeEach
    void setUp() {
        service = new TaskSchedulerService();
    }

    @Test
    void testSingleTaskNoDependencies() {

        Task a = new Task("A", 1, Set.of());
        List<Task> order = service.getExecutionOrder(Set.of(a));

        assertEquals(1, order.size());
        assertEquals("A", order.getFirst().getId());
    }

    @Test
    void testLinearDependency() {
        Task a = new Task("A", 1, Set.of());
        Task b = new Task("B", 1, Set.of("A"));

        List<Task> order = service.getExecutionOrder(Set.of(b, a));

        assertEquals(2, order.size());
        assertEquals("A", order.get(0).getId());
        assertEquals("B", order.get(1).getId());
    }

    @Test
    void testPriorityBreaksTie() {
        Task a = new Task("A", 1, Set.of());
        Task b = new Task("B", 2, Set.of("A"));
        Task c = new Task("C", 1, Set.of("A"));

        List<Task> order = service.getExecutionOrder(Set.of(a, b, c));

        assertEquals(3, order.size());
        assertEquals("A", order.get(0).getId());
        assertEquals("C", order.get(1).getId());
        assertEquals("B", order.get(2).getId());
    }

    @Test
    void testCircularDependencyThrows() {
        Task a = new Task("A", 1, Set.of("B"));
        Task b = new Task("B", 1, Set.of("A"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getExecutionOrder(Set.of(a, b)));
        assertEquals("Circular dependency", ex.getMessage());
    }

    @Test
    void testMissingDependencyThrows() {
        Task a = new Task("A", 1, Set.of("X"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.getExecutionOrder(Set.of(a)));
        assertEquals("Missing dependency", ex.getMessage());

    }

    @Test
    void testEligibleTasksNoDependencies() {
        Task a = new Task("A", 1, Set.of());
        Task b = new Task("B", 1, Set.of());

        List<Task> eligible = service.getEligibleTasks(Set.of(a, b));

        assertEquals(2, eligible.size());
    }

    @Test
    void testEligibleTasksWithPendingDependency() {
        Task a = new Task("A", 1, Set.of());
        Task b = new Task("B", 1, Set.of("A"));

        List<Task> eligible = service.getEligibleTasks(Set.of(a, b));

        assertEquals(1, eligible.size());
        assertEquals("A", eligible.getFirst().getId());
    }


    @Test
    void testEligibleTasksWithCompletedDependency() {
        Task a = new Task("A", 1, Set.of());
        a.setStatus(TaskStatus.COMPLETED);
        Task b = new Task("B", 1, Set.of("A"));

        List<Task> eligible = service.getEligibleTasks(Set.of(a, b));

        assertEquals(1, eligible.size());
        assertEquals("B", eligible.getFirst().getId());
    }

    @Test
    void testEligibleTasksOrderedByPriority() {
        Task a = new Task("A", 2, Set.of());
        Task b = new Task("B", 1, Set.of());

        List<Task> eligible = service.getEligibleTasks(Set.of(a, b));

        assertEquals("B", eligible.getFirst().getId());
        assertEquals("A", eligible.get(1).getId());
    }

    @Test
    void testEmptyTaskList() {
        List<Task> order = service.getExecutionOrder(Set.of());
        assertTrue(order.isEmpty());
    }
    @Test
    void testLargerGraph() {
        Task a = new Task("A", 2, Set.of());
        Task b = new Task("B", 2, Set.of("A"));
        Task c = new Task("C", 1, Set.of("A"));
        Task d = new Task("D", 3, Set.of("B", "C"));

        List<Task> order = service.getExecutionOrder(Set.of(a, b, c, d));

        assertEquals("A", order.get(0).getId());
        assertEquals("C", order.get(1).getId());
        assertEquals("B", order.get(2).getId());
        assertEquals("D", order.get(3).getId());
    }
}
