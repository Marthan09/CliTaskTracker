package com.marthan.service;

import com.marthan.domain.Task;
import com.marthan.domain.TaskStatus;
import com.marthan.repository.TaskRepository;
import com.marthan.util.LocalDateFormarter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private static List<Task> taskList = new ArrayList<>();
    private static Long taskId = 1L;

    public static void delete(List<String> arguments) {
        Optional<Task> byId = findById(arguments, taskList, TaskStatus.IN_PROGRESS);
        if (byId.isEmpty()) return;
        taskList.remove(byId.get());
        TaskRepository.saveListOnJson(taskList);
        System.out.println("Task deleted");
    }

    public static void listInProgress() {
        List<Task> tasksProgress = taskList.stream()
                .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
                .toList();
        if (tasksProgress.isEmpty()) {
            System.out.println("No tasks IN_PROGRESS");
            return;
        }
        System.out.println("Tasks in progress: ");
        tasksProgress.forEach(System.out::println);
    }

    public static void listDone() {
        List<Task> tasksDone = taskList.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .toList();
        if (tasksDone.isEmpty()) {
            System.out.println("No tasks DONE");
            return;
        }
        System.out.println("Tasks done: ");
        tasksDone.forEach(System.out::println);
    }

    public static void listAll() {
        if (taskList.isEmpty()) {
            System.out.println("You don't have task");
            return;
        }
        System.out.println("List all task: ");
        taskList.forEach(System.out::println);
    }

    public static void markInProgress(List<String> arguments) {
        if (findById(arguments, taskList, TaskStatus.IN_PROGRESS).isEmpty()) return;
        System.out.println("Task in progress, keep up");
    }

    public static void markInDone(List<String> arguments) {
        if (findById(arguments, taskList, TaskStatus.DONE).isEmpty()) return;
        System.out.println("Task done, congratulations");
    }

    public static void update(List<String> arguments) {
        Optional<Task> taskExist = findById(arguments, taskList, null);
        if (taskExist.isEmpty()) return;
        Task task1 = taskExist.get();
        taskList.remove(task1);
        task1.setDescription(getDescription(3, arguments));
        task1.setUpdateAt(LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));
        taskList.add(task1);
        System.out.println("New Description crated");
    }

    public static void add(List<String> arguments) {
        TaskRepository.loadId(taskId);
        var task = new Task(taskId, getDescription(2, arguments), TaskStatus.IN_PROGRESS,
                LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()),
                LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));

        taskList.add(task);
        taskId += 1;
        TaskRepository.saveId(taskId);
        TaskRepository.saveListOnJson(taskList);
        System.out.printf("Task added successfully (ID: %s) %n", task.getId());
    }

    private static Optional<Task> findById(List<String> arguments, List<Task> taskList, TaskStatus status) {
        try {
            Long id = Long.parseLong(arguments.get(2));
            Optional<Task> task1 = taskList.stream().filter(t -> t.getId().equals(id)).findFirst();

            task1.ifPresentOrElse((t) -> {
                t.setUpdateAt(LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));
                if (status != null) t.setStatus(status);
                TaskRepository.saveListOnJson(taskList);
            }, () -> {
                System.out.println("Not found Task");
            });
            return task1;
        } catch (Exception e) {
            System.out.println("Please, put a id valid");
            return Optional.empty();
        }
    }

    private static String getDescription(int startIndex, List<String> arguments) {
        String description = "";
        for (int i = startIndex; i < arguments.size(); i++) {
            description = description.concat(arguments.get(i) + " ");
        }
        return description.trim();
    }

    public static void loadListOnJson() {
        if (TaskRepository.loadListOnJson(taskList).isPresent()) {
            taskList = TaskRepository.loadListOnJson(taskList).get();
        }
    }
}