package org.monome.pages.time

import spock.lang.Specification

class MusicalSchedulerTest extends Specification {

    def "default bpm"() {
        expect:
        new MusicalScheduler().getBpm() == 120
    }

    def "setting bpm"() {
        expect:
        def ms = new MusicalScheduler()
        ms.setBpm(100)
        ms.getBpm() == 100
    }

    def "default ppq"() {
        expect:
        new MusicalScheduler().getPpq() == 96
    }

    def "setting ppq"() {
        expect:
        def ms = new MusicalScheduler()
        ms.setPpq(128)
        ms.getPpq() == 128
    }

    def "tickNanoseconds is auto-calculated from bpm and ppq"() {
        setup:
        def ms = new MusicalScheduler()

        expect:
        ms.getTickNanoseconds() == 5208333

        when:
        ms.setBpm(100)
        then:
        ms.getTickNanoseconds() == 6250000

        when:
        ms.setPpq(32)
        then:
        ms.getTickNanoseconds() == 18750000
    }

    def "scheduling a closure"() {
        def ms = new MusicalScheduler()
        def a = 0
        def b = 0
        ms.setPpq(4) // 4 pulses per quarter note

        when: "we initially schedule the tasks"
        ms.schedule(new MusicalDuration(1, 4)) {
            a++
        }

        ms.schedule(new MusicalDuration(1, 16)) {
            b++
        }

        then: "nothing should have happened yet"
        a == 0
        b == 0

        when: "tick once"
        ms.tick()

        then: "both tasks run once"
        a == 1
        b == 1

        when: "tick 3 more times"
        for (i in 0..2) {
            ms.tick()
        }

        then: "first task shouldn't have run, but second task should have run 3 times"
        a == 1
        b == 4

        when: "tick again"
        ms.tick()

        then: "both tasks should have run again"
        a == 2
        b == 5
    }

    def "stopping and starting a scheduled closure"() {
        def ms = new MusicalScheduler()
        def a = 0
        ms.setPpq(1)
        def task = ms.schedule(new MusicalDuration(1, 4)) {
            a++
        }

        when: "we tick twice"
        ms.tick()
        ms.tick()

        then: "closure runs twice"
        a == 2

        when: "we stop the task and tick twice"
        task.stop()
        ms.tick()
        ms.tick()

        then: "the closure doesn't run anymore"
        a == 2

        when: "we start the task and tick twice"
        task.start()
        ms.tick()
        ms.tick()

        then: "the closure runs again"
        a == 4
    }

    def "starting task resets the tick"() {
        def ms = new MusicalScheduler()
        def a = 0
        ms.setPpq(4)
        def task = ms.schedule(new MusicalDuration(1, 4)) {
            a++
        }

        when: "we tick twice"
        ms.tick()
        ms.tick()

        then: "our task runs once"
        a == 1

        when: "when we stop and start the task, and tick twice"
        task.stop()
        task.start()
        ms.tick()
        ms.tick()

        then: "our task runs once, again"
        a == 2
    }

    def "make sure we're protected against concurrent modifications"() {
        def ms = new MusicalScheduler()
        ms.setPpq(4)

        def task = null
        task = ms.schedule(new MusicalDuration(1, 4)) {
            task.stop()
        }

        def task2 = null
        task2 = ms.schedule(new MusicalDuration(1, 4)) {
            task2.stop()
        }

        expect: "when we tick, no exception occurs"
        ms.tick()
        ms.tick()
    }

}
