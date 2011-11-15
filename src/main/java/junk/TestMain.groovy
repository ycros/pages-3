package junk

import org.monome.pages.time.MusicalScheduler
import org.monome.pages.time.MusicalLoop
import org.monome.pages.time.MusicalDuration

def musicalScheduler = new MusicalScheduler()
def musicalLoop = new MusicalLoop(musicalScheduler)

def md(num, den) {
    new MusicalDuration(num, den)
}

Number.metaClass {
    getTh {-> new MusicalDuration(1, delegate)}
}

whole = 1.th
half = 2.th
qtr = 4.th

musicalScheduler.schedule(qtr) {
    println "beep!"
}

musicalScheduler.schedule(whole) {
    println "bam!"
}

musicalScheduler.schedule(half) {
    println "bong!"
}

musicalLoop.run()