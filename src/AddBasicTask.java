import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class AddBasicTask {

    private static final String FILE_NAME = "TaskFile.json";

    // ახალი თასკის დამატება Json ფაილში
    public static void addTask(String username) throws IOException {

        // ვკითხულობ არსებულ უკვე თასკებს ფაილიდან (101 ხაზი)
        JSONArray existingTasks = readTasksFromFile();

        // ვამოწმებ დასახელების უნიკალურობას
        for (int i = 0; i < existingTasks.length(); i++) {
            JSONObject task = existingTasks.getJSONObject(i);
            if (task.getString("username").equalsIgnoreCase(username)) { // Check case-insensitive match
                System.out.println("Task with username '" + username + "' already exists. Please choose a different username.");
                return; //თუ დასახელებაა ვასრულებ პროგრამას
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Task Name: ");
        String name = scanner.nextLine();

        System.out.print("Task Description: ");
        String description = scanner.nextLine();

        //ვაგენერირებ აიდის ცალკე მეთოდით, დავამატე რომ აიდი იყოს უნიკალური
        int taskId = generateTaskId(existingTasks);

        // შეყვანილ ინფორმაციას ვწერ ფაილში
        JSONObject newTask = new JSONObject();
        newTask.put("task_id", taskId);
        newTask.put("name", name);
        newTask.put("description", description);
        newTask.put("username", username);

        //მიმდინარე თასკს ვამატებ არსებულ თასკებს და ვწერ მთლიანად განახლებულ სიას ფაილშო
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
            System.out.println(task);
        }
    }

    // Task_id - ით თასკის მოძებნა
    public static void getTaskById(int taskId) throws IOException {
        JSONArray tasks = readTasksFromFile();
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            if (task.getInt("task_id") == taskId) {
                System.out.println(task);
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
                //შემეძლო იუზერნეიმიც შემეცვალა, მაგრამ მერე დასაჩეკი იქნება უზერნეიმის უნიკალურობა რედაქტირების შემდეგ, გვინდა ახლა ეგ? არ გვინდა
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter new task name: ");
                task.put("name", scanner.nextLine());
                System.out.print("Enter new task description: ");
                task.put("description", scanner.nextLine());

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
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
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
