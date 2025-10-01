package com.marthan.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marthan.domain.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class TaskRepository {
    private static final Path FILE_ID_PATH = Paths.get("FolderTask/taskId.txt");
    private static final Path FILE_TASK_PATH = Paths.get("FolderTask/Tasks.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveId(Long taskId) {
        try {
            if (Files.notExists(FILE_ID_PATH)) {
                Files.createFile(FILE_ID_PATH);
            }
            Files.writeString(FILE_ID_PATH, String.valueOf(taskId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveListOnJson(List<Task> taskList) {
        try {
            Path foderPath = Paths.get("FolderTask");
            if (Files.notExists(foderPath)){
                Files.createDirectory(foderPath);
            }

            if (Files.notExists(FILE_TASK_PATH)){
                Files.createFile(FILE_TASK_PATH);
            }

            String string = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskList);
            Files.writeString(FILE_TASK_PATH, string);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadId(Long taskId) {
        try {
            if (Files.exists(FILE_ID_PATH)) {
                if (Files.size(FILE_ID_PATH) > 0) {
                    taskId = Long.valueOf(Files.readString(FILE_ID_PATH));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<List<Task>> loadListOnJson(List<Task> taskList) {
        try {
            if (Files.notExists(FILE_TASK_PATH)){
                return Optional.empty();
            }

            if (Files.size(FILE_TASK_PATH) > 0) {
                ObjectMapper mapper = new ObjectMapper();
                String json = Files.readString(FILE_TASK_PATH);
                return Optional.ofNullable(taskList = mapper.readValue(json, new TypeReference<>() {
                }));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}