package junk;

import groovy.lang.Closure;

public class PageBuilder {
    public GridBuilder grid(Closure closure) {
        GridBuilder gridBuilder = new GridBuilder();
        closure.setDelegate(gridBuilder);
        closure.call();
        return gridBuilder;
    }
}
