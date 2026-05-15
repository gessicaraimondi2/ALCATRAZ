package db_lab.data;

import java.sql.*;

/**
 * DAO per le query statistiche (Op13 del progetto).
 * Non mappa un'entità specifica ma aggrega dati da più tabelle.
 */
public class DAOStatistiche {

    /**
     * Restituisce il tasso di partecipazione dei detenuti ai corsi (%).
     * Corrisponde alla prima query di Op13.
     */
    public double getTassoPartecipazione() throws SQLException {
        String sql = """
                SELECT
                    COUNT(DISTINCT i.MatricolaDetenuto) AS DetenutiIscritti,
                    COUNT(DISTINCT d.MatricolaDetenuto) AS TotaleDetenuti,
                    ROUND(100.0 * COUNT(DISTINCT i.MatricolaDetenuto)
                          / COUNT(DISTINCT d.MatricolaDetenuto), 2) AS TassoPartecipazione_Pct
                FROM DETENUTO d
                LEFT JOIN Iscrizione i ON d.MatricolaDetenuto = i.MatricolaDetenuto
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("TassoPartecipazione_Pct");
        }
        return 0.0;
    }

    /**
     * Restituisce la media di detenuti per sezione.
     * Corrisponde alla seconda query di Op13.
     */
    public double getMediaDetenutiPerSezione() throws SQLException {
        String sql = """
                SELECT ROUND(COUNT(d.MatricolaDetenuto) * 1.0
                             / COUNT(DISTINCT s.NumeroSezione), 2) AS MediaDetenuti_Per_Sezione
                FROM SEZIONE s
                LEFT JOIN DETENUTO d ON s.NumeroSezione = d.NumeroSezione
                """;
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("MediaDetenuti_Per_Sezione");
        }
        return 0.0;
    }

    /**
     * Restituisce il numero totale di prenotazioni in attesa.
     */
    public int getPrenotazioniInAttesa() throws SQLException {
        String sql = "SELECT COUNT(*) AS Totale FROM PRENOTAZIONE WHERE EsitoPrenotazione = 'In attesa'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("Totale");
        }
        return 0;
    }

    /**
     * Restituisce il numero totale di detenuti per stato della pena.
     */
    public void stampaDetenutiPerStato() throws SQLException {
        String sql = "SELECT StatoDellaPena, COUNT(*) AS Totale FROM DETENUTO GROUP BY StatoDellaPena";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("StatoDellaPena") + ": " + rs.getInt("Totale"));
            }
        }
    }
}
