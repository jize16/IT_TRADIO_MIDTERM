
package com.scc.dao;

import com.scc.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public ScenarioDao() {
        createTable();
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS patient_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    patient_name TEXT NOT NULL,
                    treatment TEXT NOT NULL,
                    amount_due REAL NOT NULL,
                    payment TEXT NOT NULL
                )
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Unable to initialize database: " + e.getMessage(), e);
        }
    }

    public boolean addPatient(ScenarioEntity patient) {
        String sql = """
                INSERT INTO patient_records
                    (patient_name, treatment, amount_due, payment)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, patient.patient_name());
            statement.setString(2, patient.treatment());
            statement.setDouble(3, patient.amount_due());
            statement.setString(4, patient.payment());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> getAllPatients() {
        List<ScenarioEntity> patients = new ArrayList<>();

        String sql = """
                SELECT id, patient_name, treatment, amount_due, payment
                FROM patient_records
                ORDER BY id
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ScenarioEntity patient = new ScenarioEntity(
                        resultSet.getInt("id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("treatment"),
                        resultSet.getDouble("amount_due"),
                        resultSet.getString("payment")
                );

                patients.add(patient);
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return patients;
    }
}
