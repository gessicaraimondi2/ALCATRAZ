package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPrenotazione {

    private final Connection connection;

    public DAOPrenotazione(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Prenotazione p) {
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_INSERT,
                p.getNumeroAutorizzazione(), p.getTipoAutorizzazione(),
                Date.valueOf(p.getData()), p.getEffAccountID(),
                p.getMatricolaDetenuto(), p.getMotivoRifiuto(),
                p.getEsitoPrenotazione().toDBString())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert prenotazione", e);
        }
    }

    public Prenotazione getByID(int idPrenotazione) {
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_GET_BY_ID, idPrenotazione);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByID prenotazione", e);
        }
        return null;
    }

    public List<Prenotazione> getByVisitatore(int accountID) {
        var list = new ArrayList<Prenotazione>();
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_GET_BY_VISITATORE, accountID);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByVisitatore prenotazione", e);
        }
        return list;
    }

    public List<Prenotazione> getByDetenuto(String matricolaDetenuto) {
        var list = new ArrayList<Prenotazione>();
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_GET_BY_DETENUTO, matricolaDetenuto);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByDetenuto prenotazione", e);
        }
        return list;
    }

    public List<Prenotazione> getInAttesa() {
        var list = new ArrayList<Prenotazione>();
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_GET_IN_ATTESA);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getInAttesa prenotazione", e);
        }
        return list;
    }

    public List<Prenotazione> getAll() {
        var list = new ArrayList<Prenotazione>();
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll prenotazioni", e);
        }
        return list;
    }

    public boolean aggiornaEsito(int idPrenotazione, Prenotazione.EsitoPrenotazione esito, String motivoRifiuto) {
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_UPDATE_ESITO,
                esito.toDBString(), motivoRifiuto, idPrenotazione)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore aggiornaEsito prenotazione", e);
        }
    }

    public boolean delete(int idPrenotazione) {
        try (var ps = DAOUtils.prepare(connection, Queries.PRENOTAZIONE_DELETE, idPrenotazione)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete prenotazione", e);
        }
    }

    private Prenotazione map(ResultSet rs) throws SQLException {
        String esito = rs.getString("EsitoPrenotazione");
        Prenotazione.EsitoPrenotazione e = switch (esito) {
            case "In attesa"  -> Prenotazione.EsitoPrenotazione.In_attesa;
            case "Confermata" -> Prenotazione.EsitoPrenotazione.Confermata;
            case "Rifiutata"  -> Prenotazione.EsitoPrenotazione.Rifiutata;
            default -> throw new SQLException("EsitoPrenotazione sconosciuto: " + esito);
        };
        return new Prenotazione(
            rs.getInt("IDPrenotazione"),
            rs.getInt("NumeroAutorizzazione"),
            rs.getString("TipoAutorizzazione"),
            rs.getDate("Data").toLocalDate(),
            rs.getInt("Eff_AccountID"),
            rs.getString("MatricolaDetenuto"),
            rs.getString("MotivoRifiuto"),
            e
        );
    }
}