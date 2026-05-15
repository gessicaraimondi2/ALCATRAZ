package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPersonale {

    public boolean insert(Personale p) throws SQLException {
        String sql = "INSERT INTO PERSONALE (Matricola, Nome, Cognome, Ruolo, DataAssunzione, AccountID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getMatricola());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getCognome());
            ps.setString(4, p.getRuolo().name());
            ps.setDate(5, Date.valueOf(p.getDataAssunzione()));
            ps.setInt(6, p.getAccountID());
            return ps.executeUpdate() > 0;
        }
    }

    public Personale getByMatricola(String matricola) throws SQLException {
        String sql = "SELECT * FROM PERSONALE WHERE Matricola = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricola);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Personale> getAll() throws SQLException {
        List<Personale> list = new ArrayList<>();
        String sql = "SELECT * FROM PERSONALE";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Personale> getByRuolo(Personale.Ruolo ruolo) throws SQLException {
        List<Personale> list = new ArrayList<>();
        String sql = "SELECT * FROM PERSONALE WHERE Ruolo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruolo.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean update(Personale p) throws SQLException {
        String sql = "UPDATE PERSONALE SET Nome=?, Cognome=?, Ruolo=?, DataAssunzione=?, AccountID=? " +
                     "WHERE Matricola=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());
            ps.setString(3, p.getRuolo().name());
            ps.setDate(4, Date.valueOf(p.getDataAssunzione()));
            ps.setInt(5, p.getAccountID());
            ps.setString(6, p.getMatricola());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String matricola) throws SQLException {
        String sql = "DELETE FROM PERSONALE WHERE Matricola = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricola);
            return ps.executeUpdate() > 0;
        }
    }

    private Personale map(ResultSet rs) throws SQLException {
        return new Personale(
            rs.getString("Matricola"),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            Personale.Ruolo.valueOf(rs.getString("Ruolo")),
            rs.getDate("DataAssunzione").toLocalDate(),
            rs.getInt("AccountID")
        );
    }
}