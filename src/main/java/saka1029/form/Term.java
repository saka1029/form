package saka1029.form;

import java.util.ArrayList;
import java.util.List;

public class Term {

    public final int k;
    public final List<Factor> factors = new ArrayList<>();

    public Term(int k) {
        this.k = k;
    }

}
