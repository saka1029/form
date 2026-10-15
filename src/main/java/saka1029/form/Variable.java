package saka1029.form;

import java.util.HashMap;
import java.util.Map;

public class Variable implements Primary {

    static final Map<String, Variable> all = new HashMap<>();
    public final String name;

    private Variable(String name) {
        this.name = name;
    }

    public static Variable of(String name) {
        return all.computeIfAbsent(name, k -> new Variable(name));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Variable v
            && v.name.equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
