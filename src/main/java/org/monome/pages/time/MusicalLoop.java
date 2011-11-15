package org.monome.pages.time;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

public class MusicalLoop extends AccurateLoop {

    private final MusicalScheduler musicalScheduler;
    private static MusicalLoop instance;

    public static MusicalLoop getInstance() {
        if (instance == null)
            instance = new MusicalLoop(MusicalScheduler.getInstance());
        return instance;
    }

    public MusicalLoop(MusicalScheduler musicalScheduler) {
        this.musicalScheduler = musicalScheduler;
    }

    int count = 0;
    SummaryStatistics stats = new SummaryStatistics();

    @Override
    protected void runTick(long delta) {
        // TODO: Work the ticks off system org.monome.pages.time, not aligned to this loop, to prevent drifting
        // Basically: delay a tick, or do two ticks if we're floating out of sync
        musicalScheduler.tick();
    }

    @Override
    protected long getTickLength() {
        return musicalScheduler.getTickNanoseconds();
    }
}
