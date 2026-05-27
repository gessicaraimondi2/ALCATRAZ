package db_lab.data;

import java.sql.*;

/**
 * Utility per i DAO — stessa struttura del progetto di esempio della prof.
 *
 * Cambiamenti rispetto a DBConnection:
 *   - La connessione viene passata dall'esterno (da App.java), non riaperta ad ogni query
 *   - prepare() elimina il boilerplate ripetuto in ogni metodo DAO
 *   - DAOException (unchecked) sostituisce SQLException nei DAO — niente "throws SQLException" ovunque
 */
public final class DAOUtils {

    /** Apre una connessione MySQL locale — chiamata UNA VOLTA SOLA in App.java. */
    public static Connection localMySQLConnection(String database, String username, String password) {
        try {
            String url = "jdbc:mysql://localhost:3306/" + database
                       + "?useSSL=false&serverTimezone=UTC";
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * Prepara uno statement con i valori già impostati.
     *
     * Uso nei DAO:
     *   var ps = DAOUtils.prepare(connection, Queries.LOGIN_AMMINISTRATORE, email, password);
     *
     * Elimina il blocco try/prepareStatement/setString ripetuto in ogni metodo.
     */
    public static PreparedStatement prepare(Connection connection, String query, Object... values)
            throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            return ps;
        } catch (Exception e) {
            if (ps != null) ps.close();
            throw e;
        }
    }
}