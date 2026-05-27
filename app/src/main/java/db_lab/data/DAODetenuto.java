package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAODetenuto {

    private final Connection connection;

    public DAODetenuto(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Detenuto d) {
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_INSERT,
                d.getMatricolaDetenuto(), d.getNome(), d.getCognome(),
                Date.valueOf(d.getDataDiNascita()), d.getCodiceFiscale(),
                Date.valueOf(d.getDataIngresso()), d.getDurataPena(),
                d.getReato(), d.getStatoDellaPena().toDBString(),
                d.getAccountID(), d.getNumeroSezione(), d.getNumeroCella())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert detenuto", e);
        }
    }

    public Detenuto getByMatricola(String matricola) {
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_GET_BY_MATRICOLA, matricola);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByMatricola detenuto", e);
        }
        return null;
    }

    public List<Detenuto> getAll() {
        var list = new ArrayList<Detenuto>();
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll detenuti", e);
        }
        return list;
    }

    public List<Detenuto> getByCella(String numeroSezione, String numeroCella) {
        var list = new ArrayList<Detenuto>();
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_GET_BY_CELLA, numeroSezione, numeroCella);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByCella detenuto", e);
        }
        return list;
    }

    public List<Detenuto> getBySezione(String numeroSezione) {
        var list = new ArrayList<Detenuto>();
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_GET_BY_SEZIONE, numeroSezione);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getBySezione detenuto", e);
        }
        return list;
    }

    public boolean update(Detenuto d) {
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_UPDATE,
                d.getNome(), d.getCognome(), Date.valueOf(d.getDataDiNascita()),
                d.getCodiceFiscale(), Date.valueOf(d.getDataIngresso()),
                d.getDurataPena(), d.getReato(), d.getStatoDellaPena().toDBString(),
                d.getNumeroSezione(), d.getNumeroCella(), d.getMatricolaDetenuto())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore update detenuto", e);
        }
    }

    public boolean delete(String matricola) {
        try (var ps = DAOUtils.prepare(connection, Queries.DETENUTO_DELETE, matricola)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete detenuto", e);
        }
    }

    private Detenuto map(ResultSet rs) throws SQLException {
        String stato = rs.getString("StatoDellaPena");
        Detenuto.StatoDellaPena s = switch (stato) {
            case "In corso"              -> Detenuto.StatoDellaPena.In_corso;
            case "Scontata"              -> Detenuto.StatoDellaPena.Scontata;
            case "In attesa di giudizio" -> Detenuto.StatoDellaPena.In_attesa_di_giudizio;
            default -> throw new SQLException("StatoDellaPena sconosciuto: " + stato);
        };
        return new Detenuto(
            rs.getString("MatricolaDetenuto"),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            rs.getDate("DataDiNascita").toLocalDate(),
            rs.getString("CodiceFiscale"),
            rs.getDate("DataIngresso").toLocalDate(),
            rs.getString("DurataPena"),
            rs.getString("Reato"),
            s,
            rs.getInt("AccountID"),
            rs.getString("NumeroSezione"),
            rs.getString("NumeroCella")
        );
    }
}