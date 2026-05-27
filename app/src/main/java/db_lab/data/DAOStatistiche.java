package db_lab.data;

import java.sql.*;

public class DAOStatistiche {

    private final Connection connection;

    public DAOStatistiche(Connection connection) {
        this.connection = connection;
    }

    public double getTassoPartecipazione() {
        try (var ps = DAOUtils.prepare(connection, Queries.STAT_TASSO_PARTECIPAZIONE);
             var rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("TassoPartecipazione_Pct");
        } catch (SQLException e) {
            throw new DAOException("Errore getTassoPartecipazione", e);
        }
        return 0.0;
    }

    public double getMediaDetenutiPerSezione() {
        try (var ps = DAOUtils.prepare(connection, Queries.STAT_MEDIA_DETENUTI_PER_SEZIONE);
             var rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("MediaDetenuti_Per_Sezione");
        } catch (SQLException e) {
            throw new DAOException("Errore getMediaDetenutiPerSezione", e);
        }
        return 0.0;
    }

    public int getPrenotazioniInAttesa() {
        try (var ps = DAOUtils.prepare(connection, Queries.STAT_PRENOTAZIONI_IN_ATTESA);
             var rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("Totale");
        } catch (SQLException e) {
            throw new DAOException("Errore getPrenotazioniInAttesa", e);
        }
        return 0;
    }

    public void stampaDetenutiPerStato() {
        try (var ps = DAOUtils.prepare(connection, Queries.STAT_DETENUTI_PER_STATO);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("StatoDellaPena") + ": " + rs.getInt("Totale"));
            }
        } catch (SQLException e) {
            throw new DAOException("Errore stampaDetenutiPerStato", e);
        }
    }
}