package saka1029.form;

import java.util.Objects;

public class Factor {

    public final int power;
    public final Primary primary;

    public Factor(int power, Primary primary) {
        this.power = power;
        this.primary = primary;
    }

    public Factor reduce() {
        return new Factor(power, primary.reduce());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Factor f
            && f.power == power
            && f.primary.equals(primary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(power, primary);
    }

    @Override
    public String toString() {
        return power == 1 ? primary.toString() : "%s^%d".formatted(primary, power);
    }

}
