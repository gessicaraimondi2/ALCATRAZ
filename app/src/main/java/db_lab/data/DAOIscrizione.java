package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOIscrizione {

    private final Connection connection;

    public DAOIscrizione(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(String matricolaDetenuto, int codiceCorso) {
        try (var ps = DAOUtils.prepare(connection, Queries.ISCRIZIONE_INSERT,
                matricolaDetenuto, codiceCorso)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert iscrizione", e);
        }
    }

    public List<Iscrizione> getByDetenuto(String matricolaDetenuto) {
        var list = new ArrayList<Iscrizione>();
        try (var ps = DAOUtils.prepare(connection, Queries.ISCRIZIONE_GET_BY_DETENUTO, matricolaDetenuto);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByDetenuto iscrizione", e);
        }
        return list;
    }

    public List<Iscrizione> getByCorso(int codiceCorso) {
        var list = new ArrayList<Iscrizione>();
        try (var ps = DAOUtils.prepare(connection, Queries.ISCRIZIONE_GET_BY_CORSO, codiceCorso);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByCorso iscrizione", e);
        }
        return list;
    }

    public boolean updateEsito(String matricolaDetenuto, int codiceCorso, Iscrizione.Esito esito) {
        try (var ps = DAOUtils.prepare(connection, Queries.ISCRIZIONE_UPDATE_ESITO,
                esito == null ? null : esito.toDBString(),
                matricolaDetenuto, codiceCorso)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore updateEsito iscrizione", e);
        }
    }

    public boolean delete(String matricolaDetenuto, int codiceCorso) {
        try (var ps = DAOUtils.prepare(connection, Queries.ISCRIZIONE_DELETE,
                matricolaDetenuto, codiceCorso)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete iscrizione", e);
        }
    }

    private Iscrizione map(ResultSet rs) throws SQLException {
        String esitoStr = rs.getString("Esito");
        Iscrizione.Esito esito = null;
        if (esitoStr != null) {
            esito = switch (esitoStr) {
                case "Superato"     -> Iscrizione.Esito.Superato;
                case "Non superato" -> Iscrizione.Esito.Non_superato;
                case "In corso"     -> Iscrizione.Esito.In_corso;
                case "Sospeso"      -> Iscrizione.Esito.Sospeso;
                default -> throw new SQLException("Esito iscrizione sconosciuto: " + esitoStr);
            };
        }
        return new Iscrizione(rs.getString("MatricolaDetenuto"), rs.getInt("CodiceCorso"), esito);
    }
}