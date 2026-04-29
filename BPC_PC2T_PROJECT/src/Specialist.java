
public class Specialist extends StaffMember implements Skill {
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

