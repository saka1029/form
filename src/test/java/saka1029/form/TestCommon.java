package saka1029.form;

import static org.junit.Assert.assertEquals;
import static saka1029.form.Common.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestCommon {

    @Test 
    public void testToBag() {
        assertEquals(Map.of(), toBag(List.of()));
        assertEquals(Map.of(1, 1), toBag(List.of(1)));
        assertEquals(Map.of("a", 1), toBag(List.of("a")));
        assertEquals(Map.of(1, 2, 2, 1), toBag(List.of(1, 1, 2)));
        assertEquals(Map.of(1, 2, 2, 3), toBag(List.of(1, 2, 2, 1, 2)));
        assertEquals(Map.of("a", 2, "b", 1), toBag(List.of("a", "a", "b")));
        assertEquals(toBag(List.of("b", "a", "a")), toBag(List.of("a", "a", "b")));
    }

    @Test 
    public void testToList() {
        assertEquals(List.of(), toList(toBag(List.of())));
        assertEquals(List.of(3), toList(toBag(List.of(3))));
        assertEquals(List.of("a"), toList(toBag(List.of("a"))));
        assertEquals(List.of(1, 1, 2), toList(toBag(List.of(1, 1, 2))));
        assertEquals(List.of("a", "a", "b"), toList(toBag(List.of("a", "a", "b"))));
    }

}
