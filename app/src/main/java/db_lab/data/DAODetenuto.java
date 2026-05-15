package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAODetenuto {

    public boolean insert(Detenuto d) throws SQLException {
        String sql = "INSERT INTO DETENUTO (MatricolaDetenuto, Nome, Cognome, DataDiNascita, CodiceFiscale, " +
                     "DataIngresso, DurataPena, Reato, StatoDellaPena, AccountID, NumeroSezione, NumeroCella) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getMatricolaDetenuto());
            ps.setString(2, d.getNome());
            ps.setString(3, d.getCognome());
            ps.setDate(4, Date.valueOf(d.getDataDiNascita()));
            ps.setString(5, d.getCodiceFiscale());
            ps.setDate(6, Date.valueOf(d.getDataIngresso()));
            ps.setString(7, d.getDurataPena());
            ps.setString(8, d.getReato());
            ps.setString(9, d.getStatoDellaPena().toDBString());
            ps.setInt(10, d.getAccountID());
            ps.setString(11, d.getNumeroSezione());
            ps.setString(12, d.getNumeroCella());
            return ps.executeUpdate() > 0;
        }
    }

    public Detenuto getByMatricola(String matricola) throws SQLException {
        String sql = "SELECT * FROM DETENUTO WHERE MatricolaDetenuto = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricola);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Detenuto> getAll() throws SQLException {
        List<Detenuto> list = new ArrayList<>();
        String sql = "SELECT * FROM DETENUTO";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Detenuto> getByCella(String numeroSezione, String numeroCella) throws SQLException {
        List<Detenuto> list = new ArrayList<>();
        String sql = "SELECT * FROM DETENUTO WHERE NumeroSezione = ? AND NumeroCella = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroSezione);
            ps.setString(2, numeroCella);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Detenuto> getBySezione(String numeroSezione) throws SQLException {
        List<Detenuto> list = new ArrayList<>();
        String sql = "SELECT * FROM DETENUTO WHERE NumeroSezione = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroSezione);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean update(Detenuto d) throws SQLException {
        String sql = "UPDATE DETENUTO SET Nome=?, Cognome=?, DataDiNascita=?, CodiceFiscale=?, " +
                     "DataIngresso=?, DurataPena=?, Reato=?, StatoDellaPena=?, " +
                     "NumeroSezione=?, NumeroCella=? WHERE MatricolaDetenuto=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNome());
            ps.setString(2, d.getCognome());
            ps.setDate(3, Date.valueOf(d.getDataDiNascita()));
            ps.setString(4, d.getCodiceFiscale());
            ps.setDate(5, Date.valueOf(d.getDataIngresso()));
            ps.setString(6, d.getDurataPena());
            ps.setString(7, d.getReato());
            ps.setString(8, d.getStatoDellaPena().toDBString());
            ps.setString(9, d.getNumeroSezione());
            ps.setString(10, d.getNumeroCella());
            ps.setString(11, d.getMatricolaDetenuto());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String matricola) throws SQLException {
        String sql = "DELETE FROM DETENUTO WHERE MatricolaDetenuto = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricola);
            return ps.executeUpdate() > 0;
        }
    }

    private Detenuto map(ResultSet rs) throws SQLException {
        String stato = rs.getString("StatoDellaPena");
        Detenuto.StatoDellaPena s = switch (stato) {
            case "In corso"              -> Detenuto.StatoDellaPena.In_corso;
            case "Scontata"              -> Detenuto.StatoDellaPena.Scontata;
            case "In attesa di giudizio" -> Detenuto.StatoDellaPena.In_attesa_di_giudizio;
            default -> throw new SQLException("Valore StatoDellaPena sconosciuto: " + stato);
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