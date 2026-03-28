import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String FILE_NAME = "data.json";
    static boolean running = true;
    static boolean dummy = false;
    static int loop = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while(running == true){
            ++loop;

            System.out.println("\n------- Menu -------");
            System.out.println("1. Add new employee");
            System.out.println("2. Add level of cooperation");
            System.out.println("3. Remove an employee");
            System.out.println("4. Search for an employee via ID");
            System.out.println("5. Use the ability of an employee according to theirs group");
            System.out.println("6. List of all employees by their surnames (aplhabet order)");
            System.out.println("7. Display statistics");
            System.out.println("8. Display the amount of employees in all groups");
            System.out.println("9. Exit the app");
            System.out.println(" \n");
    
            if(dummy){
                System.out.println("Selected choice not available, try again!");
                dummy = false;
            }

            System.out.println("Enter your choice (simple numbers): ");
            String menuSelection = sc.nextLine();

            if(menuSelection != null){
                switch (menuSelection) {
                    case "1":
                        break;
                    case "2":
                        break;
                    case "9":
                        running = false;
                        break;
                    default:
                        dummy=true;
                        break;
                    }
                menuSelection = null;
            }

        };
        
    }
}