
package com.scc.ui;

import com.scc.dao.ScenarioDao;
import com.scc.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private final Scanner scanner;
    private final ScenarioDao dao;

    public ScenarioMenu(Scanner scanner, ScenarioDao dao) {
        this.scanner = scanner;
        this.dao = dao;
    }

    public void run() {
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> recordPatient();
                case "2" -> viewPatients();
                case "3" -> {
                    System.out.println(
                            "\nExiting system... Thank you!");
                    running = false;
                }
                default ->
                        System.out.println(
                                "Invalid choice. Please select 1, 2, or 3.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n==============================================");
        System.out.println("       DENTAL CLINIC TREATMENT LOG");
        System.out.println("==============================================");
        System.out.println("[1] Record Patient Treatment");
        System.out.println("[2] View Patient Records");
        System.out.println("[3] Exit System");
        System.out.println("==============================================");
    }

    private void recordPatient() {
        System.out.println("\n--- RECORD PATIENT TREATMENT ---");

        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        System.out.print(
                "Enter Treatment (CLEANING / EXTRACTION / FILLING): ");
        String treatment = scanner.nextLine().trim().toUpperCase();

        if (!List.of("CLEANING", "EXTRACTION", "FILLING")
                .contains(treatment)) {
            System.out.println("Invalid treatment type.");
            return;
        }

        double amount;

        try {
            System.out.print("Enter Amount Due: ");
            amount = Double.parseDouble(scanner.nextLine().trim());

            if (!Double.isFinite(amount) || amount < 0) {
                System.out.println(
                        "Amount must be a valid non-negative number.");
                return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
            return;
        }

        System.out.print("Enter Payment Status (PAID / PENDING): ");
        String payment = scanner.nextLine().trim().toUpperCase();

        if (!List.of("PAID", "PENDING").contains(payment)) {
            System.out.println("Invalid payment status.");
            return;
        }

        ScenarioEntity patient =
                new ScenarioEntity(0, name, treatment, amount, payment);

        if (dao.addPatient(patient)) {
            System.out.println(
                    "\nSUCCESS: Patient treatment recorded!");
        }
    }

    private void viewPatients() {
        List<ScenarioEntity> patients = dao.getAllPatients();

        System.out.println("\n==============================================================");
        System.out.println("                 DENTAL PATIENT RECORDS");
        System.out.println("==============================================================");

        System.out.printf(
                "| %-3s | %-20s | %-12s | %12s | %-8s |%n",
                "ID", "PATIENT NAME", "TREATMENT",
                "AMOUNT DUE", "PAYMENT");

        System.out.println(
                "+-----+----------------------+--------------+--------------+----------+");

        for (ScenarioEntity patient : patients) {
            System.out.printf(
                    "| %-3d | %-20.20s | %-12s | %12.2f | %-8s |%n",
                    patient.id(),
                    patient.patient_name(),
                    patient.treatment(),
                    patient.amount_due(),
                    patient.payment()
            );
        }

        System.out.println(
                "+-----+----------------------+--------------+--------------+----------+");
        System.out.println("Total Records Displayed: " + patients.size());
    }
}
