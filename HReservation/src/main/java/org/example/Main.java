package org.example;

import org.example.service.WelcomeScreen;
import java.util.Scanner;

public class Main {

    private void start(){

    }

    public static void main(String[] args) {
        WelcomeScreen welcome = new WelcomeScreen();
        welcome.print();
        Scanner userInput = new Scanner(System.in);

    }
}
