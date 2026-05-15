package db_lab.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Singleton per la connessione al database ALCATRAZ.
 *
 * Gestisce un'unica istanza della connessione JDBC condivisa da tutti i DAO.
 * Tabelle gestite:
 *   VISITATORE, AMMINISTRATORE, PERSONALE, SEZIONE, CELLA,
 *   DETENUTO, PRENOTAZIONE, VISITA, PROVVEDIMENTO_DISCIPLINARE,
 *   CORSO_DI_REINSERIMENTO, Iscrizione, Sorveglia
 */
public class DBConnection {

    // ------------------------------------------------------------------ //
    //  Parametri di connessione  –  modifica solo questi                  //
    // ------------------------------------------------------------------ //
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/alcatraz";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";
    private static final String DRIVER      = "com.mysql.cj.jdbc.Driver";
    // ------------------------------------------------------------------ //

    /** Istanza singleton */
    private static DBConnection instance;

    /** Connessione JDBC effettiva */
    private Connection connection;

    // ------------------------------------------------------------------ //
    //  Costruttore privato                                                 //
    // ------------------------------------------------------------------ //
    private DBConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("[DBConnection] Connessione ad ALCATRAZ stabilita.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC non trovato: " + DRIVER, e);
        }
    }

    // ------------------------------------------------------------------ //
    //  getInstance – restituisce (e crea se necessario) il singleton       //
    // ------------------------------------------------------------------ //
    /**
     * Restituisce l'istanza singleton.
     * Se la connessione è chiusa o nulla, ne crea una nuova.
     *
     * @return DBConnection singleton
     * @throws SQLException se la connessione fallisce
     */
    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    // ------------------------------------------------------------------ //
    //  getConnection – restituisce l'oggetto Connection JDBC               //
    // ------------------------------------------------------------------ //
    /**
     * Restituisce la {@link Connection} JDBC da usare nei DAO.
     *
     * Esempio di utilizzo in un DAO:
     * <pre>
     *   Connection conn = DBConnection.getInstance().getConnection();
     *   PreparedStatement ps = conn.prepareStatement("SELECT ...");
     * </pre>
     *
     * @return Connection attiva
     */
    public Connection getConnection() {
        return connection;
    }

    // ------------------------------------------------------------------ //
    //  closeConnection – chiude la connessione (da chiamare al termine)    //
    // ------------------------------------------------------------------ //
    /**
     * Chiude la connessione al database e azzera il singleton.
     * Chiamare questo metodo solo alla chiusura dell'applicazione.
     */
    public static void closeConnection() {
        if (instance != null) {
            try {
                if (instance.connection != null && !instance.connection.isClosed()) {
                    instance.connection.close();
                    System.out.println("[DBConnection] Connessione chiusa.");
                }
            } catch (SQLException e) {
                System.err.println("[DBConnection] Errore durante la chiusura: " + e.getMessage());
            } finally {
                instance = null;
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Metodi di supporto per le transazioni                               //
    // ------------------------------------------------------------------ //

    /**
     * Disabilita l'auto-commit per avviare una transazione manuale.
     * Usare insieme a {@link #commit()} o {@link #rollback()}.
     *
     * @throws SQLException se l'operazione fallisce
     */
    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    /**
     * Esegue il commit della transazione corrente e ripristina l'auto-commit.
     *
     * @throws SQLException se il commit fallisce
     */
    public void commit() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /**
     * Annulla la transazione corrente e ripristina l'auto-commit.
     */
    public void rollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("[DBConnection] Errore durante il rollback: " + e.getMessage());
        }
    }
}