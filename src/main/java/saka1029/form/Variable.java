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
    public String toString() {
        return "Variable(%s)".formatted(name);
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        if (!context.containsKey(this))
            throw new FormException("variable '%s' not define", name);
        return context.get(this);
    }

    @Override
    public String toLisp() {
        return name;
    }
}
