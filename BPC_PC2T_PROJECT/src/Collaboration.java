
public class Collaboration {
	private int colleagueId;
    private Cooperationlevel level;

    public Collaboration(int colleagueId, Cooperationlevel level){
        this.colleagueId =colleagueId;
        this.level = level;
    }

    public int getColleagueId() { return colleagueId;}
    public Cooperationlevel  getLevel() { return level; }

}
