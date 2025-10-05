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

    public static void delete(List<String> arguments) {
        Optional<Task> byId = findById(arguments);
        if (byId.isEmpty()) return;
        taskList.remove(byId.get());
        System.out.println("Task deleted");
        TaskRepository.saveFiles(taskList);
    }

    public static void listIn(TaskStatus taskStatus) {
        List<Task> tasks = taskList.stream()
                .filter(t -> t.getStatus() == taskStatus)
                .toList();
        if (tasks.isEmpty()) {
            System.out.printf("No tasks %s%n", taskStatus);
            return;
        }
        System.out.printf("Tasks in progress: %s%n", taskStatus);
        tasks.forEach(System.out::println);
    }

    public static void listAll() {
        if (taskList.isEmpty()) {
            System.out.println("You don't have task");
            return;
        }
        System.out.println("All tasks: ");
        taskList.forEach(t ->
                System.out.printf("[ID %d] %s - %s%n", t.getId(), t.getDescription(), t.getStatus()));
    }

    public static void markIn(List<String> arguments, TaskStatus taskStatus) {
        Optional<Task> taskOpt = findById(arguments);
        if (taskOpt.isEmpty()) return;

        Task task = taskOpt.get();
        task.setStatus(taskStatus);
        task.setUpdateAt(LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));

        TaskRepository.saveFiles(taskList);
        System.out.printf("Task marked as %s%n", taskStatus);
    }

    public static void update(List<String> arguments) {
        Optional<Task> taskOpt = findById(arguments);
        if (taskOpt.isEmpty()) return;
        Task task = taskOpt.get();
        taskList.remove(task);
        task.setDescription(getDescription(3, arguments));
        task.setUpdateAt(LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));
        taskList.add(task);
        TaskRepository.saveFiles(taskList);
        System.out.println("New description created");
    }

    public static void add(List<String> arguments) {
        var task = new Task(TaskRepository.getTaskID(), getDescription(2, arguments), TaskStatus.IN_PROGRESS,
                LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()),
                LocalDateFormarter.formatterLocalDateTime(LocalDateTime.now()));

        taskList.add(task);
        TaskRepository.saveFiles(taskList);
        System.out.printf("Task added successfully (ID: %s) %n", task.getId());
    }

    private static Optional<Task> findById(List<String> arguments) {
        try {
            Long id = Long.parseLong(arguments.get(2));
            Optional<Task> task = taskList.stream().filter(t -> t.getId().equals(id)).findFirst();

            if (task.isEmpty()) {
                System.out.println("Not found Task");
                return Optional.empty();
            }

            return task;
        } catch (Exception e) {
            System.out.println("Please, put a id valid");
            return Optional.empty();
        }
    }
    private static String getDescription(int startIndex, List<String> arguments) {
        return String.join(" ", arguments.subList(startIndex, arguments.size()));
    }

    public static void loadFiles() {
        TaskRepository.loadFiles().ifPresent(t -> taskList = t);
    }
}