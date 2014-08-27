package ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data;

public class Timer {
    private long pauseStart;
    private long pauseTime = 0;
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void pause() {
        pauseStart = System.currentTimeMillis();
    }

    public void resume() {
        pauseTime = pauseTime + (System.currentTimeMillis() - pauseStart);
    }

    public long getTime() {
        long time = 0;
        if (startTime != 0) {
            time = System.currentTimeMillis() - startTime - pauseTime;
        }
        return time;
    }

    public long getStartTime() {
        return startTime;
    }
}
