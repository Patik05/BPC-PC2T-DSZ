import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_NAME = "data.json";
    private static final String COLAB_FILE = "collaborations.json";
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
            System.out.println("--------------------\n");

            if(unspecifiedChoice){
                System.out.println("Selected choice not available, try again!");
            }

            System.out.println("Enter your choice (simple numbers): ");
            String menuSelection = sc.nextLine();

            unspecifiedChoice = false;
            if(menuSelection != null){
                switch (menuSelection) {
                    case "1":
                        System.out.println("-- Enter the group for the new employee --");
                        System.out.println("1 - Data analytic");
                        System.out.println("2 - Security specialist");
                        System.out.println("--------------------------------------\n");

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
                        System.out.println("Enter first employee ID to add coleague: "); int staffId = sc.nextInt();
                        System.out.println("Enter second employee ID to connect: "); int colleagueId = sc.nextInt();
                        System.out.println("Level (1-Good, 2-Avg, 3-Bad): "); int l = sc.nextInt();
                        sc.nextLine();

                        Cooperationlevel lvl = (l == 1)? Cooperationlevel.GOOD : (l==3 ? Cooperationlevel.BAD :Cooperationlevel.AVERAGE);

                        Specialist firstemployee = null;
                        Specialist secondemployee = null;
                        for(Specialist s: employees){
                            if(s.getId() == staffId) firstemployee = s;
                            if(s.getId() == colleagueId) secondemployee = s;
                        }

                        if(firstemployee != null && secondemployee != null){
                            firstemployee.addCollaboration(colleagueId, lvl);
                            secondemployee.addCollaboration(staffId, lvl);
                            System.out.println("Connection added.");
                            saveDataToFile();
                        }
                        else{
                            System.out.println("Connection failed");
                        }
                        break;
                    case "3":
                        List<Specialist> sortedForDisplay = new ArrayList<>(employees);
                        sortedForDisplay.sort((s1, s2) -> Integer.compare(s1.getId(), s2.getId()));
                        
                        System.out.println(StaffMember.getHeader());
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
                            for(Specialist s: employees) s.removeCollaboration(removeId);
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
                                System.out.println(StaffMember.getHeader());
                                System.out.println(s.toFormattedString());
                                found = true;
                                break;
                            }
                        }
                        if(!found) {
                            System.out.println("Employee not found.");
                        }
                        break;
                    case "5": {
                        System.out.println("\n--- Available Employees ---");
                        System.out.println(StaffMember.getHeader());
                        for (Specialist s : employees) {
                            System.out.println(s.toFormattedString());
                        }
                        
                        System.out.println("Enter employee ID to trigger skill: ");
                        int selectedId = sc.nextInt();
                        sc.nextLine();

                        Specialist targetEmp = null;
                        for (Specialist s : employees) {
                            if (s.getId() == selectedId) {
                                targetEmp = s;
                                break;
                            }
                        }

                        if (targetEmp == null) {
                            System.out.println("Employee not found.");
                            break;
                        }

                        if ("dataAnalyst".equals(targetEmp.getPosition())) {
                            List<Integer> targetCollabs = new ArrayList<>();
                            for (Collaboration c : targetEmp.collaborations) {
                                targetCollabs.add(c.getColleagueId());
                            }

                            if (targetCollabs.isEmpty()) {
                                System.out.println("Data Analyst " + targetEmp.getName() + " has no collaborations.");
                            } else {
                                int maxMutuals = -1;
                                Specialist bestColleague = null;

                                for (int collabId : targetCollabs) {
                                    Specialist colleague = null;
                                    for (Specialist s : employees) {
                                        if (s.getId() == collabId) {
                                            colleague = s;
                                            break;
                                        }
                                    }

                                    if (colleague != null) {
                                        int mutualCount = 0;
                                        for (Collaboration cCollab : colleague.collaborations) {
                                            if (targetCollabs.contains(cCollab.getColleagueId())) {
                                                mutualCount++;
                                            }
                                        }

                                        if (mutualCount > maxMutuals) {
                                            maxMutuals = mutualCount;
                                            bestColleague = colleague;
                                        }
                                    }
                                }

                                if (bestColleague != null) {
                                    System.out.println("Collaborator with the most mutual connections is " + 
                                                       bestColleague.getName() + " " + bestColleague.getSurname() + 
                                                       " (ID: " + bestColleague.getId() + ") with " + maxMutuals + " mutuals.");
                                } else {
                                    System.out.println("Could not determine mutual collaborators.");
                                }
                            }
                        } else if ("securitySpecialist".equals(targetEmp.getPosition())) {
                            targetEmp.executeSkill();
                        }
                        break;
                    }
                    case "6":
                        List<Specialist> sortedList = new ArrayList<>(employees);
                        sortedList.sort((s1, s2) -> s1.getSurname().compareToIgnoreCase(s2.getSurname()));
                        System.out.println(StaffMember.getHeader());
                        for(Specialist s : sortedList) {
                            System.out.println(s.toFormattedString());
                        }
                        break;
                    case "7":
                    	boolean inStats = true;
                        while (inStats) {
                        	System.out.println("\n---- Statistics ----");
                        	System.out.println("1 - Collaborations sorted by ID");
                        	System.out.println("2 - Collaborations sorted by staff name");
                        	System.out.println("3 - Collaborations sorted by relationship");
                        	System.out.println("4 - Collaborations for specific ID");
                        	System.out.println("5 - Back to main menu");
                            System.out.println("--------------------\n");
                        	
                        	if(unspecifiedChoice){
                                System.out.println("Selected choice not available, try again!");
                            }

                            System.out.println("Enter your choice (simple numbers): ");
                            String statSelection = sc.nextLine();
                            
                            unspecifiedChoice = false;
                            List<String[]> allStats = new ArrayList<>();
                            int targetId = -1;
                            
                            if (!statSelection.equals("4") && !statSelection.equals("5")) {
                                for(Specialist spec : employees) {
                                    for (Collaboration col : spec.collaborations) {
                                        allStats.add(prepareRow(spec, col, employees));
                                    }
                                }
                            }

                            switch (statSelection) {
                                case "1":
                                    allStats.sort(Comparator.comparingInt(a -> Integer.parseInt(a[0])));
                                    break;
                                case "2":
                                    allStats.sort(Comparator.comparing(a -> a[1].toLowerCase()));
                                    break;
                                case "3":
                                    allStats.sort(Comparator.comparingInt(a -> Integer.parseInt(a[5])));
                                    break;
                        		case "4":{
                        			System.out.println("Enter staffID: ");
                        			String inputID = sc.nextLine();
                        			targetId = Integer.parseInt(inputID);
                        			boolean foundId = false;	
                        			
                        			for (Specialist s : employees) {
                        				if (s.getId() == targetId) {
                        					foundId = true;
                        					break;
                        				}
                        			}
                        			if (!foundId) {
                        				System.out.println("Employee not found.");
                        				continue;
                        			}
                        			for (Specialist spec : employees) {
                                        for (Collaboration col : spec.collaborations) {
                                            if (spec.getId() == targetId) {
                                                allStats.add(prepareRow(spec, col, employees));
                                            }
                                        }
                                    }
                        			allStats.sort(Comparator.comparingInt(a -> Integer.parseInt(a[5])));
                        		} break;
                        		case "5":
                        			inStats =false;
                        			continue;
                        	}
                        	
                        	
                        	
                        	if (allStats.isEmpty()) {
                        		System.out.println("No collaborations found in the system.");
                        	}else {
                        		printTable(allStats, sc);
                        	}         	
                        }
                
                        break;
                    case "8":
                        int dataAnalystCount = 0;
                        int securitySpecialistCount = 0;

                        for (Specialist s : employees) {
                            if ("dataAnalyst".equals(s.getPosition())) {
                                dataAnalystCount++;
                            } else if ("securitySpecialist".equals(s.getPosition())) {
                                securitySpecialistCount++;
                            }
                        }

                        System.out.println("\n---- Employee Group Counts ----");
                        System.out.println("Data Analysts (Group 1): " + dataAnalystCount);
                        System.out.println("Security Specialists (Group 2): " + securitySpecialistCount);
                        System.out.println("-------------------------------\n");
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
            for (int i = 0; i < employees.size(); i++) {
                writer.write("  " + employees.get(i).toJson());
                if (i < employees.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write("\n");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (FileWriter writer = new FileWriter(COLAB_FILE)) {
            writer.write("[\n");
        	List<String> allCollabs = new ArrayList<>();
        	for(Specialist s : employees) {
        		String cJson = s.collaborationToJson();
        		if(!cJson.isEmpty()) {
        			allCollabs.add(cJson);
        		}
        	}
            	
            writer.write(String.join(", \n", allCollabs));
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
                    int parsedYearOfBirth = Integer.parseInt(line.split("\"yearOfBirth\":")[1].split(",")[0]);
                    
                    int wg = parsedRole.equals("dataAnalyst") ? 1 : 2;
                    employees.add(new Specialist(parsedId, wg, parsedName, parsedSurname, parsedYearOfBirth));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File collabFile = new File(COLAB_FILE);
        if (!collabFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(collabFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	if (line.contains("\"staffId\":")) {
            		int parsedsId = Integer.parseInt(line.split("\"staffId\":")[1].split(",")[0].trim());
            		int parsedcId = Integer.parseInt(line.split("\"colleagueId\":")[1].split(",")[0].trim());
            		String levelStr = line.split("\"level\":\"")[1].split("\"")[0];
            		
            		Cooperationlevel level = Cooperationlevel.valueOf(levelStr);
            		
            		for (StaffMember e: employees) {
            			if (e.getId()==parsedsId) {
            				e.addCollaboration(parsedcId, level);
            				break;
            			}
            		}
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
   private static String[] prepareRow(Specialist spec, Collaboration col, List<Specialist> employees) {
        String colleagueName = "Unknown";
        for (Specialist s : employees) {
            if (s.getId() == col.getColleagueId()) {
                colleagueName = s.getName() + " " + s.getSurname();
                break;
            }
        }
        return new String[]{
            String.valueOf(spec.getId()), 
            spec.getName() + " " + spec.getSurname(), 
            String.valueOf(col.getColleagueId()), 
            colleagueName, 
            col.getLevel().toString(),
            String.valueOf(col.getLevel().getRiskValue())
        };
    }
    private static void printTable(List<String[]> data, Scanner sc) {
        System.out.println("\n" + "=".repeat(95));
        System.out.printf("%-5s | %-20s | %-12s | %-20s | %-10s%n", "ID", "Staff Member", "Colleague ID", "Colleague Name", "Level");
        System.out.println("-".repeat(95));
        
        int count = 0;
        for (String[] row : data) {
            System.out.printf("%-5s | %-20s | %-12s | %-20s | %-10s%n", row[0], row[1], row[2], row[3], row[4]);
            count++;
            if (count % 20 == 0 && count < data.size()) {
                System.out.println("\n--- Press Enter to show more (" + count + "/" + data.size() + ") ---");
                sc.nextLine();
            }
        }
        System.out.println("=".repeat(95));
        System.out.println("Total: " + data.size() + " records.");
    }
}
