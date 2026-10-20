package saka1029.form;

import java.util.Map;

public class PowerImpl implements Power {

    public final int power;
    public final Primary primary;

    public PowerImpl(int power, Primary primary) {
        this.power = power;
        this.primary = primary;
    }

    @Override
    public int value(Map<Variable, Integer> context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'value'");
    }

    @Override
    public String toDebugString() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toDebugString'");
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
