package org.example;

import org.example.service.Application;
import org.example.service.WelcomeScreen;

public class Main {

    public static void main(String[] args) {
        WelcomeScreen welcome = new WelcomeScreen();
        welcome.print();
        if (welcome.isAuthenticated()) {
            Application application = new Application();
            application.launch(welcome.getLogin().getEmail());
        }
    }
}
