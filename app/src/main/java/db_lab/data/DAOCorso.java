package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCorso {

    private final Connection connection;

    public DAOCorso(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Corso c) {
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_INSERT,
                c.getTitolo(), c.getDescrizione(),
                Date.valueOf(c.getDataInizio()), Date.valueOf(c.getDataFine()),
                c.getTipologia().name(), c.getAccountID(), c.getMatricola())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert corso", e);
        }
    }

    public Corso getByCodice(int codiceCorso) {
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_GET_BY_ID, codiceCorso);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByCodice corso", e);
        }
        return null;
    }

    public List<Corso> getAll() {
        var list = new ArrayList<Corso>();
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll corsi", e);
        }
        return list;
    }

    public List<Corso> getByEducatore(String matricola) {
        var list = new ArrayList<Corso>();
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_GET_BY_EDUCATORE, matricola);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByEducatore corso", e);
        }
        return list;
    }

    public boolean update(Corso c) {
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_UPDATE,
                c.getTitolo(), c.getDescrizione(),
                Date.valueOf(c.getDataInizio()), Date.valueOf(c.getDataFine()),
                c.getTipologia().name(), c.getMatricola(), c.getCodiceCorso())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore update corso", e);
        }
    }

    public boolean delete(int codiceCorso) {
        try (var ps = DAOUtils.prepare(connection, Queries.CORSO_DELETE, codiceCorso)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete corso", e);
        }
    }

    private Corso map(ResultSet rs) throws SQLException {
        return new Corso(
            rs.getInt("CodiceCorso"),
            rs.getString("Titolo"),
            rs.getString("Descrizione"),
            rs.getDate("DataInizio").toLocalDate(),
            rs.getDate("DataFine").toLocalDate(),
            Corso.Tipologia.valueOf(rs.getString("Tipologia")),
            rs.getInt("AccountID"),
            rs.getString("Matricola")
        );
    }
}