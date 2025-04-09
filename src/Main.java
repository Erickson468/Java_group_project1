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

                AddBasicTask.addTask(username);
                break;
            case "2":
                AddBasicTask.getAllTasks();
                break;
            case "3":
                System.out.print("Enter task ID to view: ");
                int taskId = Integer.parseInt(scanner.nextLine());
                AddBasicTask.getTaskById(taskId);
                break;
            case "4":
                System.out.print("Enter task ID to update: ");
                int updateId = Integer.parseInt(scanner.nextLine());
                AddBasicTask.updateTask(updateId);
                break;
            case "5":
                System.out.print("Enter task ID to delete: ");
                int deleteId = Integer.parseInt(scanner.nextLine());
                AddBasicTask.deleteTask(deleteId);
                break;
            case "6":
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
}
}

