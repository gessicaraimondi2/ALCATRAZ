package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOIscrizione {

    /**
     * Op9 – Iscrivere un detenuto a un corso
     * INSERT INTO Iscrizione (MatricolaDetenuto, CodiceCorso, Esito) VALUES (?, ?, NULL);
     */
    public boolean insert(String matricolaDetenuto, int codiceCorso) throws SQLException {
        String sql = "INSERT INTO Iscrizione (MatricolaDetenuto, CodiceCorso, Esito) VALUES (?, ?, NULL)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricolaDetenuto);
            ps.setInt(2, codiceCorso);
            return ps.executeUpdate() > 0;
        }
    }

    /** Tutte le iscrizioni di un detenuto (con titolo corso). */
    public List<Iscrizione> getByDetenuto(String matricolaDetenuto) throws SQLException {
        List<Iscrizione> list = new ArrayList<>();
        String sql = "SELECT MatricolaDetenuto, CodiceCorso, Esito FROM Iscrizione WHERE MatricolaDetenuto = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricolaDetenuto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Tutti i detenuti iscritti a un corso. */
    public List<Iscrizione> getByCorso(int codiceCorso) throws SQLException {
        List<Iscrizione> list = new ArrayList<>();
        String sql = "SELECT MatricolaDetenuto, CodiceCorso, Esito FROM Iscrizione WHERE CodiceCorso = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codiceCorso);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Aggiorna l'esito di un'iscrizione. */
    public boolean updateEsito(String matricolaDetenuto, int codiceCorso, Iscrizione.Esito esito) throws SQLException {
        String sql = "UPDATE Iscrizione SET Esito = ? WHERE MatricolaDetenuto = ? AND CodiceCorso = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, esito == null ? null : esito.toDBString());
            ps.setString(2, matricolaDetenuto);
            ps.setInt(3, codiceCorso);
            return ps.executeUpdate() > 0;
        }
    }

    /** Elimina un'iscrizione. */
    public boolean delete(String matricolaDetenuto, int codiceCorso) throws SQLException {
        String sql = "DELETE FROM Iscrizione WHERE MatricolaDetenuto = ? AND CodiceCorso = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricolaDetenuto);
            ps.setInt(2, codiceCorso);
            return ps.executeUpdate() > 0;
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
                default -> throw new SQLException("Valore Esito sconosciuto: " + esitoStr);
            };
        }
        return new Iscrizione(rs.getString("MatricolaDetenuto"), rs.getInt("CodiceCorso"), esito);
    }
}