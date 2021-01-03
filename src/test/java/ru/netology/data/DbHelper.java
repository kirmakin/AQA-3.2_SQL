package ru.netology.data;

import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {
    public static Connection getConnection() {
        try {
            final Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/app", "kirmakin", "pass");
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public static String getVerificationCode(String login) {
        String userId = null;
        val dataSQL = "SELECT id FROM users WHERE login = ?;";
        try (val conn = getConnection();
             val idStmt = conn.prepareStatement(dataSQL);
        ) {
            idStmt.setString(1, login);
            try (val rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getString("id");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String code = null;
        val authCode = "SELECT code FROM auth_codes WHERE user_id = ? order by created desc limit 1;";
        try (val conn = getConnection();
             val codeStmt = conn.prepareStatement(authCode);
        ) {
            codeStmt.setString(1, userId);
            try (val rs = codeStmt.executeQuery()) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return code;
    }

    public static String getStatusFromDb(String login) {
        String statusSQL = "SELECT status FROM users WHERE login = ?;";
        String status = null;
        try (val conn = getConnection();
             val statusStmt = conn.prepareStatement(statusSQL);) {
            statusStmt.setString(1, login);
            try (val rs = statusStmt.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }

    public static void cleanDb() {
        String deleteCards = "DELETE FROM cards; ";
        String deleteAuthCodes = "DELETE FROM auth_codes; ";
        String deleteUsers = "DELETE FROM users; ";
        try (val conn = DbHelper.getConnection();
             val deleteCardsStmt = conn.createStatement();
             val deleteAuthCodesStmt = conn.createStatement();
             val deleteUsersStmt = conn.createStatement();
        ) {
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteAuthCodesStmt.executeUpdate(deleteAuthCodes);
            deleteUsersStmt.executeUpdate(deleteUsers);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
