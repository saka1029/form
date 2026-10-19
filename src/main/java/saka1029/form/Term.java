package saka1029.form;

import java.util.Set;

public interface Term extends Value, ToDebugString {
    int coefficient();
    Set<Factor> factors();
}
