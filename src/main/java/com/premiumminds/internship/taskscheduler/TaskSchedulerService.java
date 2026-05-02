package com.premiumminds.internship.taskscheduler;

import java.util.*;

public class TaskSchedulerService implements ITaskSchedulerService {
    private static final String CIRCULAR_DEPENDENCY_MSG = "Circular dependency";
    private static final String MISSING_DEPENDENCY_MSG = "Missing dependency";
    @Override
    public List<Task> getEligibleTasks(Collection<Task> tasks) {
        HashMap<String,Task> taskMap = new HashMap<>();
        for(Task task:tasks){
            taskMap.put(task.getId(),task);
        }

        List<Task> eligibleTasks = new ArrayList<>();
        for(Task task: tasks){
            if(task.getStatus()==TaskStatus.PENDING && areDependenciesCompleted(task,taskMap))
                eligibleTasks.addLast(task);
        }
        eligibleTasks.sort(Comparator.comparingInt(Task::getPriority));
        return eligibleTasks;
    }

    private boolean areDependenciesCompleted(Task task, HashMap<String, Task> taskMap) {
        Set<String> dependencies = task.getDependencies();
        for(String dependency: dependencies){
            Task dep = taskMap.get(dependency);
            if(dep == null || !dep.getStatus().equals(TaskStatus.COMPLETED))
                return false;
        }
        return true;
    }

    @Override
    public List<Task> getExecutionOrder(Collection<Task> tasks) {
        checkMissingDependencies(tasks);

        PriorityQueue<Task> pq = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority));
        List<Task> executionOrderedTasks = new LinkedList<>();
        HashMap<String, Integer> dependenciesNum = new HashMap<>();

        //Gets the number of dependencies of each task and stores it on dependenciesNum
        for(Task task: tasks){
            dependenciesNum.put(task.getId(),task.getDependencies().size());
            if(task.getDependencies().isEmpty())
                pq.add(task);
        }

        //Process tasks without dependencies and decreases the dependency number of dependent tasks
        while(!pq.isEmpty()) {
            Task t = pq.poll();
            executionOrderedTasks.addLast(t);
            for (Task task : tasks) {
                if (task.getDependencies().contains(t.getId())) {
                    String id = task.getId();
                    int dependencies = dependenciesNum.get(id) -1;
                    dependenciesNum.put(id,dependencies);
                    if (dependencies == 0)
                        pq.add(task);
                }
            }
        }
        //if lists sizes differ some tasks are not executable because of circularity
        if (executionOrderedTasks.size() != dependenciesNum.size())
            throw new IllegalArgumentException(CIRCULAR_DEPENDENCY_MSG);
        return executionOrderedTasks;
    }

    private void checkMissingDependencies(Collection<Task> tasks) {
        Set<String> validIds = new HashSet<>();
        for(Task task:tasks){
            validIds.add(task.getId());
        }

        for(Task task:tasks){
            for(String id:task.getDependencies()){
                if(!validIds.contains(id))
                    throw new IllegalArgumentException(MISSING_DEPENDENCY_MSG);
            }
        }

    }
}
