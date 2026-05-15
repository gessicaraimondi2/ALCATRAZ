package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPrenotazione {

    public boolean insert(Prenotazione p) throws SQLException {
        String sql = "INSERT INTO PRENOTAZIONE (NumeroAutorizzazione, TipoAutorizzazione, Data, " +
                     "Eff_AccountID, MatricolaDetenuto, MotivoRifiuto, EsitoPrenotazione) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getNumeroAutorizzazione());
            ps.setString(2, p.getTipoAutorizzazione());
            ps.setDate(3, Date.valueOf(p.getData()));
            ps.setInt(4, p.getEffAccountID());
            ps.setString(5, p.getMatricolaDetenuto());
            ps.setString(6, p.getMotivoRifiuto());   // può essere null
            ps.setString(7, p.getEsitoPrenotazione().toDBString());
            return ps.executeUpdate() > 0;
        }
    }

    public Prenotazione getByID(int idPrenotazione) throws SQLException {
        String sql = "SELECT * FROM PRENOTAZIONE WHERE IDPrenotazione = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Prenotazione> getByVisitatore(int accountID) throws SQLException {
        List<Prenotazione> list = new ArrayList<>();
        String sql = "SELECT * FROM PRENOTAZIONE WHERE Eff_AccountID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Prenotazione> getByDetenuto(String matricolaDetenuto) throws SQLException {
        List<Prenotazione> list = new ArrayList<>();
        String sql = "SELECT * FROM PRENOTAZIONE WHERE MatricolaDetenuto = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricolaDetenuto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Prenotazione> getInAttesa() throws SQLException {
        List<Prenotazione> list = new ArrayList<>();
        String sql = "SELECT * FROM PRENOTAZIONE WHERE EsitoPrenotazione = 'In attesa'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    /** Aggiorna l'esito (conferma o rifiuta) e imposta eventuale motivo rifiuto */
    public boolean aggiornaEsito(int idPrenotazione, Prenotazione.EsitoPrenotazione esito,
                                  String motivoRifiuto) throws SQLException {
        String sql = "UPDATE PRENOTAZIONE SET EsitoPrenotazione=?, MotivoRifiuto=? WHERE IDPrenotazione=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, esito.toDBString());
            ps.setString(2, motivoRifiuto);
            ps.setInt(3, idPrenotazione);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int idPrenotazione) throws SQLException {
        String sql = "DELETE FROM PRENOTAZIONE WHERE IDPrenotazione = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            return ps.executeUpdate() > 0;
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
