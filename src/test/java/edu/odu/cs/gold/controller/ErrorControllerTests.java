package edu.odu.cs.gold.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorControllerTests {

    private ErrorController errorController;
    private HttpServletRequest request501;
    private HttpServletRequest request404;
    private HttpServletRequest request500;

    @Before
    public void setup() {
        request501 = mock(HttpServletRequest.class);
        when(request501.getAttribute("javax.servlet.error.status_code")).thenReturn(501);

        request404 = mock(HttpServletRequest.class);
        when(request404.getAttribute("javax.servlet.error.status_code")).thenReturn(404);

        request500 = mock(HttpServletRequest.class);
        when(request500.getAttribute("javax.servlet.error.status_code")).thenReturn(500);

        errorController = new ErrorController();
    }

    @Test
    public void testError() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = errorController.error(
                null,
                null,
                null,
                null,
                request501,
                model);

        assertEquals("error/error.html",returnURL);

    }

    @Test
    public void testError404() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = errorController.error(
                null,
                null,
                null,
                null,
                request404,
                model);

        assertEquals("error/404.html",returnURL);

    }

    @Test
    public void testError500() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = errorController.error(
                null,
                null,
                null,
                null,
                request500,
                model);

        assertEquals("error/500.html",returnURL);

    }
}
