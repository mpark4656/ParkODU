package edu.odu.cs.gold.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

import static org.junit.Assert.assertEquals;

public class HomeControllerTests {

    private HomeController homeController;

    @Before
    public void setup() {
        homeController = new HomeController();
    }

    @Test
    public void testIndex() {
        String returnURL = homeController.index();
        assertEquals("home/index", returnURL);
    }

    @Test
    public void testSettings() {
        String returnURL = homeController.settings();
        assertEquals("settings/index", returnURL);
    }

    @Test
    public void testLogin() {
        String returnURL = homeController.login();
        assertEquals("home/login", returnURL);
    }

    @Test
    public void testLoginError() {
        ExtendedModelMap model = new ExtendedModelMap();
        String returnURL = homeController.loginError(model);
        assertEquals("home/login", returnURL);
    }

    @Test
    public void testLogout() {
        String returnURL = homeController.logout();
        assertEquals("redirect:/", returnURL);
    }
}
