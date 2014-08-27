package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

public class Phase {
    private int phaseId;
    private long time;
    private long startTime;
    private long endTime;
    private int load;

    public Phase(int aPhaseId, long aStartTime, long aTime, int aLoad) {
        phaseId = aPhaseId;;
        startTime = aStartTime;
        time = aTime;
        endTime = startTime + time;
        load = aLoad;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public long getTimeRemaining (long actualTime) {
        return endTime - actualTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getLoad() {
        return load;
    }
}
