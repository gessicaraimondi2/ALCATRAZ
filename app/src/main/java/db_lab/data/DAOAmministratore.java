package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per AMMINISTRATORE — riscritto seguendo il pattern della prof.
 *
 * Differenze rispetto alla versione originale:
 *   1. Riceve la Connection nel costruttore — non riapre la connessione ad ogni query
 *   2. Usa DAOUtils.prepare() — niente try/prepareStatement/setString ripetuto
 *   3. Usa Queries.* — le SQL non sono inline nel codice
 *   4. Lancia DAOException (unchecked) — niente "throws SQLException" nella firma
 */
public class DAOAmministratore {

    private final Connection connection;

    public DAOAmministratore(Connection connection) {
        this.connection = connection;
    }

    // ------------------------------------------------------------------ //

    public boolean insert(Amministratore a) {
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_INSERT,
                a.getEmail(), a.getPassword(), a.getNome(), a.getCognome(), a.getMatricola())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert amministratore", e);
        }
    }

    public Amministratore getByID(int accountID) {
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_GET_BY_ID, accountID);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByID amministratore", e);
        }
        return null;
    }

    public List<Amministratore> getAll() {
        var list = new ArrayList<Amministratore>();
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll amministratori", e);
        }
        return list;
    }

    public Amministratore login(String email, String password) {
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_LOGIN, email, password);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore login amministratore", e);
        }
        return null;
    }

    public boolean update(Amministratore a) {
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_UPDATE,
                a.getEmail(), a.getPassword(), a.getNome(), a.getCognome(),
                a.getMatricola(), a.getAccountID())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore update amministratore", e);
        }
    }

    public boolean delete(int accountID) {
        try (var ps = DAOUtils.prepare(connection, Queries.AMMINISTRATORE_DELETE, accountID)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete amministratore", e);
        }
    }

    // ------------------------------------------------------------------ //

    private Amministratore map(ResultSet rs) throws SQLException {
        return new Amministratore(
            rs.getInt("AccountID"),
            rs.getString("E_Mail"),
            rs.getString("Password"),
            rs.getDate("DataCreazione").toLocalDate(),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            rs.getString("Matricola")
        );
    }
}