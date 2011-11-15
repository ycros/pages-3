package org.monome.pages.time;

import org.apache.commons.math.stat.descriptive.StatisticalSummary;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.monome.pages.time.MusicalScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AccurateLoop {

    private boolean running = false;

    public AccurateLoop() {
    }

    public void run() {
        if (!performanceHasBeenAdjusted)
            adjustForSystemPerformance();

        running = true;

        SummaryStatistics stats = new SummaryStatistics();

        try {
            long end = 0;
            long scheduleDuration = 0;
            int iterations = 0;
            long padding = 0;

            while (running) {
                long start = System.nanoTime();
                long overSleep = 0;
                long delta = 0;
                if (end != 0) {
                    delta = start - end;
                    overSleep = delta - scheduleDuration;
                }

                runTick(delta);

                long originalScheduleDuration = getTickLength();
                end = System.nanoTime();
                scheduleDuration = (originalScheduleDuration - (end - start)) - overSleep + padding;
                sleepNanos(scheduleDuration);

                // Self adjust the loop to try to get it to what it should be
                if (iterations >= 10) {
                    long mean = (long)stats.getMean();
                    padding = originalScheduleDuration - mean;
                    stats.clear();
                    iterations = 0;
                } else {
                    iterations++;
                    stats.addValue(delta);
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    public void stop() {
        running = false;
    }

    protected abstract void runTick(long delta);

    protected abstract long getTickLength();

    private static long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
    private static long SPIN_YIELD_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
    private static boolean performanceHasBeenAdjusted = false;

    protected static void sleepNanos (long nanoDuration) throws InterruptedException {
        final long end = System.nanoTime() + nanoDuration;
        long timeLeft = nanoDuration;
        do {

            if (timeLeft > SLEEP_PRECISION)
                Thread.sleep (1);
            else
                if (timeLeft > SPIN_YIELD_PRECISION)

                    Thread.sleep(0);

            timeLeft = end - System.nanoTime();

            if (Thread.interrupted())
                throw new InterruptedException ();

        } while (timeLeft > 0);

    }

    private static StatisticalSummary measurePrecisionInMicroseconds(long millisecondsToSleep) {
        final int ITERATIONS = 1000;

        SummaryStatistics statistics = new SummaryStatistics();

        try {
            for (int i = 0; i < ITERATIONS; i++) {
                long start = System.nanoTime();
                Thread.sleep(millisecondsToSleep);

                // Skip first 10 for warm-up
                if (i > 10)
                    statistics.addValue((System.nanoTime() - start)/1000);
            }
        } catch (InterruptedException ex) {
            return null;
        }

        return statistics;
    }

    public static void adjustForSystemPerformance() {
        Logger logger = LoggerFactory.getLogger(AccurateLoop.class);
        logger.info("Adjusting timing for system performance...");

        // Find best sleep duration performance
        {
            long sleepDurationMicroseconds = 0;
            StatisticalSummary stats = measurePrecisionInMicroseconds(1);

            sleepDurationMicroseconds = Math.round(stats.getMean() + stats.getStandardDeviation()*2);

            SLEEP_PRECISION = TimeUnit.MICROSECONDS.toNanos(sleepDurationMicroseconds);
        }

        // Find spin yield performance
        {
            StatisticalSummary stats = measurePrecisionInMicroseconds(0);
            double accuracy = stats.getMean() + stats.getStandardDeviation()*2;

            SPIN_YIELD_PRECISION = TimeUnit.MICROSECONDS.toNanos(Math.round(accuracy));
        }

        logger.info("New timing precisions: sleep: {}µs, spin: {}.µs", SLEEP_PRECISION/1000, SPIN_YIELD_PRECISION/1000);
    }

}
