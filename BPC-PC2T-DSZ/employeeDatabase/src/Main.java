import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Skill {
    void executeSkill();
}

abstract class StaffMember {
    private int id;
    protected int workGroup;
    private String name;
    private String surname;
    private int yearOfBirth;

    public StaffMember(int id, int workGroup, String name,  String surname, int yearOfBirth) {
        this.id = id;
        this.workGroup = workGroup;
        this.name = name;
        this.surname = surname;
        this.yearOfBirth = yearOfBirth;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }

    public abstract String getPosition();

    public String toJson() {
        return "{\"id\":" + id +
            ", \"role\":\"" + getPosition() +
            "\", \"name\":\"" + name +
            "\", \"surname\":\"" + surname +
            "\", \"yearOfBirth\":" + yearOfBirth +
            "}";
    }

    public String toFormattedString() {
        return "| " + id + " | " + name + " | " + surname + " | " + yearOfBirth + " |";
    }
}

class Specialist extends StaffMember implements Skill {
    public Specialist(int id, int workGroup, String name, String surname, int yearOfBirth) {
        super(id, workGroup, name, surname, yearOfBirth);
    }

    @Override
    public String getPosition() {
        if(workGroup == 1){
            return "dataAnalyst";
        }else{
            return "securitySpecialist";
        }
    }

    @Override
    public void executeSkill() {
        System.out.println("Analyzing security for: " + getName());
    }
}

public class Main {
    private static final String FILE_NAME = "data.json";
    static List<Specialist> employees = new ArrayList<>();
    static boolean running = true;
    static boolean unspecifiedChoice = false;
    static int loop = 0;
    static Scanner sc = new Scanner(System.in);
    private static int yearOfBirth = 0, workGroup = 0;
    private static String name, surname;

    public static void main(String[] args) {
        loadDataFromFile();

        while(running == true){
            ++loop;
            System.out.println("\n------- Menu -------");
            System.out.println("1. Add new employee");
            System.out.println("2. Add level of cooperation");
            System.out.println("3. Remove an employee");
            System.out.println("4. Search for an employee via ID");
            System.out.println("5. Use the ability of an employee according to theirs group");
            System.out.println("6. List of all employees by their surnames (alphabet order)");
            System.out.println("7. Display statistics");
            System.out.println("8. Display the amount of employees in all groups");
            System.out.println("9. Exit the app");
            System.out.println(" \n");

            if(unspecifiedChoice){
                System.out.println("Selected choice not available, try again!");
            }

            System.out.println("Enter your choice (simple numbers): ");
            String menuSelection = sc.nextLine();

            unspecifiedChoice = false;
            if(menuSelection != null){
                switch (menuSelection) {
                    case "1":
                        System.out.println("Enter the group for the new employee");
                        System.out.println("1 - Data analytic, 2 - Security specialist");
                        workGroup = sc.nextInt();
                        sc.nextLine();

                        System.out.println("Enter the employee name: ");
                        name = sc.nextLine();

                        System.out.println("Enter the employee surname: ");
                        surname = sc.nextLine();

                        System.out.println("Enter the employee year of birth: ");
                        yearOfBirth = sc.nextInt();
                        sc.nextLine();
                        
                        int assignedId = getNextAvailableId();
                        Specialist newSpec = new Specialist(assignedId, workGroup, name, surname, yearOfBirth);
                        employees.add(newSpec);
                        saveDataToFile();
                        break;
                    case "2":
                        break;
                    case "3":
                        List<Specialist> sortedForDisplay = new ArrayList<>(employees);
                        sortedForDisplay.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
                        
                        int maxId = 0;
                        for(Specialist s : sortedForDisplay) {
                            System.out.println(s.toFormattedString());
                            if(s.getId() > maxId) {
                                maxId = s.getId();
                            }
                        }
                        System.out.println("Available IDs range: 1 to " + maxId);
                        System.out.println("Enter employee ID to remove: ");
                        int removeId = sc.nextInt();
                        sc.nextLine();
                        boolean removed = employees.removeIf(e -> e.getId() == removeId);
                        if(removed) {
                            System.out.println("Employee removed.");
                            saveDataToFile();
                        } else {
                            System.out.println("Employee not found.");
                        }
                        break;
                    case "4":
                        System.out.println("Enter employee ID to search: ");
                        int searchId = sc.nextInt();
                        sc.nextLine();
                        boolean found = false;
                        for(Specialist s : employees) {
                            if(s.getId() == searchId) {
                                System.out.println(s.toFormattedString());
                                found = true;
                                break;
                            }
                        }
                        if(!found) {
                            System.out.println("Employee not found.");
                        }
                        break;
                    case "6":
                        List<Specialist> sortedList = new ArrayList<>(employees);
                        sortedList.sort((s1, s2) -> s1.getSurname().compareToIgnoreCase(s2.getSurname()));
                        for(Specialist s : sortedList) {
                            System.out.println(s.toFormattedString());
                        }
                        break;
                    case "9":
                        running = false;
                        break;
                    default:
                        unspecifiedChoice=true;
                        break;
                }
            }
        };
    }

    private static int getNextAvailableId() {
        int candidate = 1;
        while (true) {
            boolean isUsed = false;
            for (Specialist s : employees) {
                if (s.getId() == candidate) {
                    isUsed = true;
                    break;
                }
            }
            if (!isUsed) {
                return candidate;
            }
            candidate++;
        }
    }

    private static void saveDataToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("[\n");
            for(int i = 0; i < employees.size(); i++) {
                writer.write("  " + employees.get(i).toJson());
                if(i < employees.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"id\":")) {
                    int parsedId = Integer.parseInt(line.split("\"id\":")[1].split(",")[0].trim());
                    String parsedRole = line.split("\"role\":\"")[1].split("\"")[0];
                    String parsedName = line.split("\"name\":\"")[1].split("\"")[0];
                    String parsedSurname = line.split("\"surname\":\"")[1].split("\"")[0];
                    int parsedYearOfBirth = Integer.parseInt(line.split("\"yearOfBirth\":")[1].split("}")[0].trim());
                    
                    int wg = parsedRole.equals("dataAnalyst") ? 1 : 2;
                    employees.add(new Specialist(parsedId, wg, parsedName, parsedSurname, parsedYearOfBirth));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}