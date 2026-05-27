package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOVisitatore {

    private final Connection connection;

    public DAOVisitatore(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Visitatore v) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_INSERT,
                v.getEmail(), v.getPassword(), Date.valueOf(v.getDataCreazione()),
                v.getNome(), v.getCognome(), v.getCodiceFiscale())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert visitatore", e);
        }
    }

    public Visitatore getByID(int accountID) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_GET_BY_ID, accountID);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByID visitatore", e);
        }
        return null;
    }

    public Visitatore getByEmail(String email) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_GET_BY_EMAIL, email);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByEmail visitatore", e);
        }
        return null;
    }

    public List<Visitatore> getAll() {
        var list = new ArrayList<Visitatore>();
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll visitatori", e);
        }
        return list;
    }

    public Visitatore login(String email, String password) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_LOGIN, email, password);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore login visitatore", e);
        }
        return null;
    }

    public boolean update(Visitatore v) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_UPDATE,
                v.getEmail(), v.getPassword(), v.getNome(), v.getCognome(),
                v.getCodiceFiscale(), v.getAccountID())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore update visitatore", e);
        }
    }

    public boolean delete(int accountID) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITATORE_DELETE, accountID)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete visitatore", e);
        }
    }

    private Visitatore map(ResultSet rs) throws SQLException {
        return new Visitatore(
            rs.getInt("AccountID"),
            rs.getString("E_Mail"),
            rs.getString("Password"),
            rs.getDate("DataCreazione").toLocalDate(),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            rs.getString("CodiceFiscale")
        );
    }
}