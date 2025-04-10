import java.io.IOException;
import java.util.Scanner;


public class Main {

public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Task Manager");
    System.out.println("1. Add Task");
    System.out.println("2. Show All Tasks");
    System.out.println("3. Get Task by ID");
    System.out.println("4. Update Task by ID");
    System.out.println("5. Delete Task by ID");
    System.out.println("6. Exit");

    boolean running = true;
    while (running) {
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.print("Enter your username: ");
                String username = scanner.nextLine();

                System.out.println("Select task type:");
                System.out.println("1. Add Basic Task");
                System.out.println("2. Add Limited Task");
                System.out.println("3. Add Repeatable Task");

                String taskType = scanner.nextLine();

                switch (taskType) {
                    case "1":
                        AddBasicTask.addTask(username, "basic");
                        break;
                    case "2":
                        AddBasicTask.addTask(username, "limited");
                        break;
                    case "3":
                        AddBasicTask.addTask(username, "repeatable");
                        break;
                    default:
                        System.out.println("Invalid task type selection.");
                }
                break;
            case "2":
                AddBasicTask.getAllTasks();
                break;
            case "3":
                System.out.print("Enter task ID to view: ");
                String viewIdStr = scanner.nextLine();
                if (isNumeric(viewIdStr)) {
                    int viewId = Integer.parseInt(viewIdStr);
                    AddBasicTask.getTaskById(viewId);
                } else {
                    System.out.println("Invalid ID.");
                }
                break;
            case "4":
                System.out.print("Enter task ID to update: ");
                String updateIdStr = scanner.nextLine();
                if (isNumeric(updateIdStr)) {
                    int updateId = Integer.parseInt(updateIdStr);
                    AddBasicTask.updateTask(updateId);
                } else {
                    System.out.println("Invalid ID.");
                }
                break;
            case "5":
                System.out.print("Enter task ID to delete: ");
                String deleteIdStr = scanner.nextLine();
                if (isNumeric(deleteIdStr)) {
                    int deleteId = Integer.parseInt(deleteIdStr);
                    AddBasicTask.deleteTask(deleteId);
                } else {
                    System.out.println("Invalid ID.");
                }
                break;
            case "6":
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
}

    private static boolean isNumeric(String str) {
        return str.matches("\\d+"); // \\d+ არის ნებისმიერი ციფრი 0-დან 9-მდე
    }
}

