import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddBasicTask {

    private static final String FILE_NAME = "TaskFile.json";

    // ახალი თასკის დამატება Json ფაილში
    public static void addTask(String username, String taskType) throws IOException {
        JSONArray existingTasks = readTasksFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Task Name: ");
        String name = scanner.nextLine();

        // ვამოწმებ დასახელების უნიკალურობას
        for (int i = 0; i < existingTasks.length(); i++) {
            JSONObject task = existingTasks.getJSONObject(i);
            if (task.getString("name").equalsIgnoreCase(name)) {
                System.out.println("Task with name '" + name + "' already exists. Please choose a different name.");
                return;
            }
        }

        System.out.print("Task Description: ");
        String description = scanner.nextLine();

        int taskId = generateTaskId(existingTasks);

        JSONObject newTask = new JSONObject();
        newTask.put("task_id", taskId);
        newTask.put("name", name);
        newTask.put("description", description);
        newTask.put("username", username);
        newTask.put("type", taskType); // ვამატებთ task-ის ტიპს JSON-ში

        // task-ის ტიპის მიხედვით ვამატებთ დამატებით ველებს
        switch (taskType.toLowerCase()) {
            case "limited":
                System.out.print("Enter deadline (yyyy-MM-ddTHH:mm, e.g. 2025-04-15T14:30): ");
                String deadlineInput = scanner.nextLine();
                try {
                    LocalDateTime deadline = LocalDateTime.parse(deadlineInput);
                    newTask.put("deadline", deadline.toString());
                } catch (Exception e) {
                    System.out.println("Invalid deadline format. Task was not saved.");
                    return;
                }
                break;

            case "repeatable":
                System.out.print("Enter repeat count: ");
                try {
                    int repeatCount = Integer.parseInt(scanner.nextLine());
                    newTask.put("repeat_count", repeatCount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Task was not saved.");
                    return;
                }
                break;

            case "basic":
                // არაფერს ვამატებთ, უკვე ყველაფერი შევიყვანეთ
                break;

            default:
                System.out.println("Unknown task type. Task was not saved.");
                return;
        }

        existingTasks.put(newTask);
        writeTasksToFile(existingTasks);

        System.out.println("Task saved successfully!");
    }

    // ყველა თასკის ნახვა
    public static void getAllTasks() throws IOException {
        JSONArray tasks = readTasksFromFile();
        //სიმრავლის სიგრძეს ვჩეკავ და ვაბრუნებ
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            System.out.println(task.toString(4));
        }
    }

    // Task_id - ით თასკის მოძებნა
    public static void getTaskById(int taskId) throws IOException {
        JSONArray tasks = readTasksFromFile();
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.getInt("task_id") == taskId) {
                System.out.println(task.toString(4));
                return;
            }
        }
        System.out.println("Task with ID " + taskId + " not found.");
    }

    // task_id-ით აფდეითი
    public static void updateTask(int taskId) throws IOException {
        JSONArray tasks = readTasksFromFile();
        boolean taskFound = false; //შემოვიღე ბულეანი რომ დავჩეკო აიდის ვალიდურობა


        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.getInt("task_id") == taskId) { //თუ შეყვანილი აიდი ემთხვევა რომელიმეს შეუძლია რედაქტირება
                taskFound = true;

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter new task name: ");
                String newName = scanner.nextLine();


                // ვამოწმებ დასახელების უნიკალურობას
                for (int j = 0; j < tasks.length(); j++) {
                    JSONObject otherTask = tasks.getJSONObject(j);
                    if (otherTask.getString("name").equalsIgnoreCase(newName)) {
                        System.out.println("Task with name '" + newName + "' already exists. Please choose a different name.");
                        return;
                    }
                }

                task.put("name", newName); // აქ ვიყენებთ უკვე წაკითხულს

                System.out.print("Enter new task description: ");
                task.put("description", scanner.nextLine());

                // განახლებები task_type-ის მიხედვით
                if (task.has("type")) {
                    String type = task.getString("type");

                    if (type.equalsIgnoreCase("repeatable")) {
                        System.out.print("Enter new repeat count: ");
                        String repeatCountStr = scanner.nextLine();
                        if (repeatCountStr.matches("\\d+")) { //ასევე ვამოწმებ რომ იყოს ციფრი
                            task.put("repeat_count", Integer.parseInt(repeatCountStr));
                        } else {
                            System.out.println("Invalid repeat count. Must be a number.");
                            return;
                        }
                    } else if (type.equalsIgnoreCase("limited")) {
                        System.out.print("Enter new deadline (yyyy-MM-ddTHH:mm): ");
                        String deadline = scanner.nextLine();
                        try {
                            LocalDateTime.parse(deadline, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            task.put("deadline", deadline);
                        } catch (Exception e) {
                            System.out.println("Invalid deadline format. Please use format: yyyy-MM-ddTHH:mm");
                            return;
                        }
                    }
                }

                // დააფდეითებულის უკან ჩაწერა
                writeTasksToFile(tasks);
                System.out.println("Task updated successfully!");
                return;
            }
        }
        if (!taskFound) {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    // task_id-ით ვშლით თასკებს
    public static void deleteTask(int taskId) throws IOException {
        JSONArray tasks = readTasksFromFile();
        boolean taskFound = false; //შემოვიღე ბულეანი რომ დავჩეკო აიდის ვალიდურობა
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.getInt("task_id") == taskId) {
                taskFound = true;
                tasks.remove(i);
                // განახლებული ცხრილის უკან დაბრუნება
                writeTasksToFile(tasks);
                System.out.println("Task deleted successfully!");
                return;
            }
        }
        if (!taskFound) {
            System.out.println("Task with ID " + taskId + " not found.");
        }
    }

    // წაკითხვა ფაილიდან
    private static JSONArray readTasksFromFile() throws IOException {
        File file = new File(FILE_NAME);
        JSONArray tasks = new JSONArray();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                if (content.length() > 0) {
                    tasks = new JSONArray(content.toString());
                }
            }
        }
        return tasks;
    }

    // თასკების დამატება ფაილში
    private static void writeTasksToFile(JSONArray tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(tasks.toString(4)); // Pretty print
        }
    }


    private static int generateTaskId(JSONArray existingTasks) {
        int taskId;
        boolean isUnique;

        do {
            taskId = (int) (Math.random() * 10000);  // რენდომ თასკ აიდი
            isUnique = true;

            // ვჩეკავ აიდის უნიკალურობას
            for (int i = 0; i < existingTasks.length(); i++) {
                JSONObject task = existingTasks.getJSONObject(i);
                if (task.getInt("task_id") == taskId) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);  // თუ უნიკალური არ არის მანამდე დაილუფება სანამ უნიკალური არ დაისეტება

        return taskId;
    }


}