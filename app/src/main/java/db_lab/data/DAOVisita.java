package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOVisita {

    private final Connection connection;

    public DAOVisita(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Visita v) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_INSERT,
                v.getIdPrenotazione(), Date.valueOf(v.getData()),
                Time.valueOf(v.getOrario()), v.getAccountID(),
                v.getEsitoVisita().name())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert visita", e);
        }
    }

    public Visita getByID(int numeroVisita) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_GET_BY_ID, numeroVisita);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByID visita", e);
        }
        return null;
    }

    public Visita getByPrenotazione(int idPrenotazione) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_GET_BY_PRENOTAZIONE, idPrenotazione);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByPrenotazione visita", e);
        }
        return null;
    }

    public List<Visita> getAll() {
        var list = new ArrayList<Visita>();
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll visite", e);
        }
        return list;
    }

    public List<Visita> getEffettuate() {
        var list = new ArrayList<Visita>();
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_GET_EFFETTUATE);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getEffettuate visite", e);
        }
        return list;
    }

    public boolean aggiornaEsito(int numeroVisita, Visita.EsitoVisita esito) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_UPDATE_ESITO,
                esito.name(), numeroVisita)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornaEsito visita", e);
        }
    }

    public boolean delete(int numeroVisita) {
        try (var ps = DAOUtils.prepare(connection, Queries.VISITA_DELETE, numeroVisita)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete visita", e);
        }
    }

    private Visita map(ResultSet rs) throws SQLException {
        return new Visita(
            rs.getInt("NumeroVisita"),
            rs.getInt("IDPrenotazione"),
            rs.getDate("Data").toLocalDate(),
            rs.getTime("Orario").toLocalTime(),
            rs.getInt("AccountID"),
            Visita.EsitoVisita.valueOf(rs.getString("EsitoVisita"))
        );
    }
}