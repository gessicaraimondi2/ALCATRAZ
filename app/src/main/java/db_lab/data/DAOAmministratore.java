package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOAmministratore {

    public boolean insert(Amministratore a) throws SQLException {
        String sql = "INSERT INTO AMMINISTRATORE (E_Mail, Password, DataCreazione, Nome, Cognome, Matricola) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getEmail());
            ps.setString(2, a.getPassword());
            ps.setDate(3, Date.valueOf(a.getDataCreazione()));
            ps.setString(4, a.getNome());
            ps.setString(5, a.getCognome());
            ps.setString(6, a.getMatricola());
            return ps.executeUpdate() > 0;
        }
    }

    public Amministratore getByID(int accountID) throws SQLException {
        String sql = "SELECT * FROM AMMINISTRATORE WHERE AccountID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Amministratore> getAll() throws SQLException {
        List<Amministratore> list = new ArrayList<>();
        String sql = "SELECT * FROM AMMINISTRATORE";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Amministratore login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM AMMINISTRATORE WHERE E_Mail = ? AND Password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public boolean update(Amministratore a) throws SQLException {
        String sql = "UPDATE AMMINISTRATORE SET E_Mail=?, Password=?, Nome=?, Cognome=?, Matricola=? " +
                     "WHERE AccountID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getEmail());
            ps.setString(2, a.getPassword());
            ps.setString(3, a.getNome());
            ps.setString(4, a.getCognome());
            ps.setString(5, a.getMatricola());
            ps.setInt(6, a.getAccountID());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int accountID) throws SQLException {
        String sql = "DELETE FROM AMMINISTRATORE WHERE AccountID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            return ps.executeUpdate() > 0;
        }
    }

    private Amministratore map(ResultSet rs) throws SQLException {
        return new Amministratore(
            rs.getInt("AccountID"),
            rs.getString("E_Mail"),
            rs.getString("Password"),
            rs.getDate("DataCreazione").toLocalDate(),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            rs.getString("Matricola")
        );
    }
}
