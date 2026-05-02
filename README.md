# Task Scheduling System

Solution for the Premium Minds Summer Internship Exercise 2026.

## How to run tests
./mvnw test

## Implementation
- `getExecutionOrder` - Returns tasks in valid execution order using Kahn's algorithm
- `getEligibleTasks` - Returns tasks eligible for execution ordered by priority

## Edge cases handled
- Circular dependencies
- Missing dependency references
