
package com.scc;

import com.scc.dao.ScenarioDao;
import com.scc.ui.ScenarioMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ScenarioDao dao = new ScenarioDao();
            ScenarioMenu menu = new ScenarioMenu(scanner, dao);

            menu.run();

        } catch (IllegalStateException e) {
            System.out.println("Unable to start the system.");
            System.out.println(e.getMessage());
        }
    }
}
