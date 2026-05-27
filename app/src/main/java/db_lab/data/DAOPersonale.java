package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPersonale {

    private final Connection connection;

    public DAOPersonale(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Personale p) {
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_INSERT,
                p.getMatricola(), p.getNome(), p.getCognome(),
                p.getRuolo().name(), Date.valueOf(p.getDataAssunzione()), p.getAccountID())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert personale", e);
        }
    }

    public Personale getByMatricola(String matricola) {
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_GET_BY_MATRICOLA, matricola);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByMatricola personale", e);
        }
        return null;
    }

    public List<Personale> getAll() {
        var list = new ArrayList<Personale>();
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll personale", e);
        }
        return list;
    }

    public List<Personale> getByRuolo(Personale.Ruolo ruolo) {
        var list = new ArrayList<Personale>();
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_GET_BY_RUOLO, ruolo.name());
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByRuolo personale", e);
        }
        return list;
    }

    public boolean update(Personale p) {
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_UPDATE,
                p.getNome(), p.getCognome(), p.getRuolo().name(),
                Date.valueOf(p.getDataAssunzione()), p.getAccountID(), p.getMatricola())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore update personale", e);
        }
    }

    public boolean delete(String matricola) {
        try (var ps = DAOUtils.prepare(connection, Queries.PERSONALE_DELETE, matricola)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete personale", e);
        }
    }

    private Personale map(ResultSet rs) throws SQLException {
        return new Personale(
            rs.getString("Matricola"),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            Personale.Ruolo.valueOf(rs.getString("Ruolo")),
            rs.getDate("DataAssunzione").toLocalDate(),
            rs.getInt("AccountID")
        );
    }
}