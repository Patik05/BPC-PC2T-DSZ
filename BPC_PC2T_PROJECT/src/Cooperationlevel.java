
public enum Cooperationlevel {
	GOOD(1), AVERAGE(2), BAD(3);

    private final int riskValue;
    Cooperationlevel(int riskValue) { this.riskValue = riskValue; }
    public int getRiskValue() { return riskValue; }
}

