package db_lab.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton per la connessione al database ALCATRAZ.
 */
public class DBConnection {

    // ------------------------------------------------------------------ //
    //  Parametri di connessione                                          //
    // ------------------------------------------------------------------ //
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/alcatraz";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER      = "com.mysql.cj.jdbc.Driver";

    // ------------------------------------------------------------------ //
    //  Singleton                                                         //
    // ------------------------------------------------------------------ //
    private static Connection connection;

    // ------------------------------------------------------------------ //
    //  getConnection STATICO – usato da tutti i DAO                      //
    // ------------------------------------------------------------------ //
    /**
     * Restituisce una connessione attiva al database.
     * Se la connessione è nulla o chiusa, ne crea una nuova.
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("[DBConnection] Connessione ad ALCATRAZ stabilita.");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC non trovato: " + DRIVER, e);
        }
        return connection;
    }

    // ------------------------------------------------------------------ //
    //  closeConnection – chiude la connessione                           //
    // ------------------------------------------------------------------ //
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DBConnection] Connessione chiusa.");
                }
            } catch (SQLException e) {
                System.err.println("[DBConnection] Errore durante la chiusura: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Metodi transazionali                                              //
    // ------------------------------------------------------------------ //
    public static void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    public static void rollback() {
        try {
            getConnection().rollback();
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("[DBConnection] Errore durante il rollback: " + e.getMessage());
        }
    }
}
