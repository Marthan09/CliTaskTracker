package com.marthan;

import com.marthan.service.TaskService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskService.loadListOnJson();
        Scanner keyboard = new Scanner(System.in);

        while (true) {
            String text = keyboard.nextLine();
            List<String> arguments = Arrays.asList(text.split(" "));

            if (!arguments.getFirst().equals("task-cli")) {
                System.out.println("Error please, put command task-cli <arguments>");
                continue;
            }

            String nameOperation = arguments.size() > 1 ? arguments.get(1) : " ";

            switch (nameOperation) {
                case "add" -> TaskService.add(arguments);
                case "update" -> TaskService.update(arguments);
                case "mark-in-done" -> TaskService.markInDone(arguments);
                case "mark-in-progress" -> TaskService.markInProgress(arguments);
                case "list" -> TaskService.listAll();
                case "list-done" -> TaskService.listDone();
                case "list-in-progress" -> TaskService.listInProgress();
                case "delete" -> TaskService.delete(arguments);
            }
            System.out.printf("please select a command <ADD, UPDATE, DELETE>.%nRun again");
            return;
        }
    }
}