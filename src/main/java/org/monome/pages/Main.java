package org.monome.pages;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.monome.pages.groovy.ScriptBase;
import org.monome.pages.time.MusicalLoop;

import java.io.IOException;

public class Main {

    // TODO: refactor all the common stuff out into classes
    public static void main(String[] args) {
        String[] roots = new String[] { "/Users/ycros/tmp/" };
        try {
            CompilerConfiguration config = new CompilerConfiguration();
            config.setScriptBaseClass(ScriptBase.class.getName());

            GroovyClassLoader loader = new GroovyClassLoader(Main.class.getClassLoader(), config);

            GroovyScriptEngine gse = new GroovyScriptEngine(roots, loader);
            gse.setConfig(config);

            Binding binding = new Binding();
            gse.run("test.groovy", binding);

            MusicalLoop.getInstance().run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
