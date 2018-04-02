package edu.odu.cs.gold.mongo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueWrapperTests {

    @Test
    public void testValueWrapper() {
        String value = "Test";
        ValueWrapper valueWrapper = new ValueWrapper(value);
        assertEquals(value, valueWrapper.getValue());
        String value2 = "Test2";
        valueWrapper.setValue(value2);
        assertEquals(value2, valueWrapper.getValue());
    }
}
