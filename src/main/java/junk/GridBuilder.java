package junk;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.List;

public class GridBuilder {
    List<Closure> presses = new ArrayList<Closure>();

    public void press(Closure closure) {
        presses.add(closure);
    }
}
