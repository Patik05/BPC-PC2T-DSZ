import java.util.ArrayList;
import java.util.List;

public abstract class StaffMember {
    private int id;
    protected int workGroup;
    private String name;
    private String surname;
    private int yearOfBirth;

    protected List<Collaboration> collaborations = new ArrayList<>();

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

    public void addCollaboration(int colleagueId, Cooperationlevel level){
        collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
        this.collaborations.add(new Collaboration(colleagueId, level));
    }

    public void removeCollaboration(int colleagueId){
        this.collaborations.removeIf(c -> c.getColleagueId() == colleagueId);
    }

    public abstract String getPosition();

    public String toJson() {
        return "{\"id\":" + id +
            ", \"role\":\"" + getPosition() +
            "\", \"name\":\"" + name +
            "\", \"surname\":\"" + surname +
            "\", \"yearOfBirth\":" + yearOfBirth +
            ", \"collaborationCount\":" + collaborations.size() +
            "}";
    }

    public String toFormattedString() {
        return "| " + id + " | " + name + " | " + surname + " | " + yearOfBirth + " |" + collaborations.size() + " |";
    }
    
    public String collaborationToJson() {
    	String json="";
    	for (int i =0; i< collaborations.size(); i++) {
    		Collaboration c = collaborations.get(i);
    		
    		json+="{\"staffId\":" + this.id +
    			  ", \"colleagueId\":" + c.getColleagueId() +
    			  ", \"level\":\"" + c.getLevel()+ "\"}";
    		
    		if (i<collaborations.size()- 1){
    			json += ",\n";
    		}
    	}
    	return json;
    }
}

