package tomrowicki.util;

public class Time {

    public static final float timeStarted = System.nanoTime();

    /**
     *
     * @return elapsed time in seconds
     */
    public static float getTime() {
        return (float)((System.nanoTime() - timeStarted) * 1E-9);
    }
}
