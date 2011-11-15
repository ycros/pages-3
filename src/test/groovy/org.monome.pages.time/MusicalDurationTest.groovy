package org.monome.pages.time

import org.apache.commons.math.fraction.Fraction
import spock.lang.Specification
import org.monome.pages.time.MusicalDuration


class MusicalDurationTest extends Specification {

    def frac(a, b) {
        new Fraction(a, b)
    }

    def "value comes out how we put it in"() {
        expect:
        assert new MusicalDuration(a, b).getValue().equals(frac(a, b))

        where:
        a | b
        1 | 2
        1 | 4
        2 | 1
    }

    def "dotting returns a new dotted duration"() {
        expect:
        def md = new MusicalDuration(a, b)
        assert md.dotted().getValue().equals(frac(c, d))
        assert md.getValue().equals(frac(a, b))

        where:
        a | b | c | d
        1 | 4 | 3 | 8
        1 | 8 | 3 | 16
        2 | 1 | 3 | 1
    }

    def "double dotting returns a new double dotted duration"() {
        expect:
        def md = new MusicalDuration(a, b)
        assert md.doubleDotted().getValue().equals(frac(c, d))
        md.getValue().equals(frac(a, b))

        where:
        a | b | c | d
        1 | 4 | 7 | 16
        1 | 8 | 7 | 32
        2 | 1 | 7 | 2
    }

    def "triple dotting returns a new triple dotted duration"() {
        expect:
        def md = new MusicalDuration(a, b)
        assert md.tripleDotted().getValue().equals(frac(c, d))
        md.getValue().equals(frac(a, b))

        where:
        a | b | c | d
        1 | 4 | 15 | 32
        1 | 8 | 15 | 64
        2 | 1 | 15 | 4
    }

    def "get number of pulses for a ppq value"() {
        expect:
        def md = new MusicalDuration(a, b)
        md.getPulses(ppq) == pulses

        where:
        a | b  | ppq | pulses
        1 | 4  | 7   | 7
        1 | 2  | 7   | 14
        2 | 1  | 3   | 24
        1 | 8  | 4   | 2
        1 | 16 | 4   | 1
    }

}
