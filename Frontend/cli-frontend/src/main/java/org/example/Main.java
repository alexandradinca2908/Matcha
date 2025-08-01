package org.example;

import org.example.loggerobjects.*;
import org.example.textprocessors.AnsiColors;
import org.example.api.*;
import org.example.menu.MenuOption;
import org.example.menu.views.View;
import org.example.menu.views.ViewID;
import org.example.menu.views.ViewManager;
import org.example.userinterface.UIView;

import java.util.Scanner;

import static org.example.textprocessors.InputTranslator.translateInput;

public class Main {
    public static void main(String[] args) {
        ViewManager viewManager = ViewManager.getInstance();
        viewManager.disableAccounts(true);  // set this to false to enable accounts

        LogManager.getInstance().initLoggers();
        System.out.println(AnsiColors.toPurple("Welcome to Matcha Reddit!\nPlease choose an option:"));

        boolean isActive = true;
        Scanner scan;
        String option;
        MenuOption translatedInput;

        while (isActive) {
            View currentViewObject = viewManager.getCurrentViewObject();
            ViewID currentViewID = viewManager.getCurrentViewID();

            currentViewObject.displayMenu();

            scan = new Scanner(System.in);
            option = scan.nextLine();
            translatedInput = translateInput(option, currentViewID, viewManager.isLoggedIn());

            isActive = currentViewObject.activateMenuOption(translatedInput);
        }
    }
}