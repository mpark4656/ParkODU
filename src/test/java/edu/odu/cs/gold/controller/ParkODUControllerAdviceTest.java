package edu.odu.cs.gold.controller;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ParkODUControllerAdviceTest {

    private ParkODUControllerAdvice parkODUControllerAdvice;

    @Before
    public void setup() {
        parkODUControllerAdvice = new ParkODUControllerAdvice();
    }

    @Test
    public void testError() {
        String returnURL = parkODUControllerAdvice.error();
        assertEquals("/error/error.html",returnURL);

    }

}
