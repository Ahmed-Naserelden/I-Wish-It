package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.Server;

/**
 * DbManager class to manage database connections and operations.
 * Handles opening and closing connections, and preparing SQL statements.
 */
public class DbManager {

    private static final String DB_URL = "jdbc:derby://localhost:1527/dbn45contacts";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    private static Connection conn;

    /**
     * Opens a connection to the database.
     * Initializes the connection using the specified URL, username, and password.
     */
    public static void openConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Server.logPass("Connection to database is established");
        } catch (SQLException e) {
            Server.logFail("Error while connecting to the database server.");
            Server.logInfo("  -   DB URL: " + DB_URL);
            Server.logInfo("  - Username: " + DB_USERNAME);
            Server.logInfo("  - Password: " + DB_PASSWORD);
            Server.logExit("Cannot connect to the database!");
            System.exit(1);
        }
    }

    /**
     * Closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            Server.logFail(e.getMessage());
        }
    }

    /**
     * Prepares an SQL statement for execution.
     *
     * @param query The SQL query to prepare.
     * @return A PreparedStatement object for executing the query.
     * @throws SQLException If an SQL error occurs while preparing the statement.
     */
    public static PreparedStatement prepareStatement(String query) throws SQLException {
        return conn.prepareStatement(query);
    }
}
