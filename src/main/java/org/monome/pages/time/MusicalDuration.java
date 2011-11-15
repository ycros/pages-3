package org.monome.pages.time;

import org.apache.commons.math.fraction.Fraction;

public class MusicalDuration {

    private final Fraction value;

    public MusicalDuration(int numerator, int denominator) {
        this.value = new Fraction(numerator, denominator);
    }

    private MusicalDuration(Fraction fraction) {
        this.value = fraction;
    }

    public Fraction getValue() {
        return value;
    }

    public MusicalDuration dotted() {
        return new MusicalDuration(value.add(value.divide(2)));
    }

    public MusicalDuration doubleDotted() {
        return new MusicalDuration(value.add(value.multiply(new Fraction(3, 4))));
    }

    public MusicalDuration tripleDotted() {
        return new MusicalDuration(value.add(value.multiply(new Fraction(7, 8))));
    }

    public int getPulses(int ppq) {
        return value.multiply(ppq * 4).intValue();
    }

}