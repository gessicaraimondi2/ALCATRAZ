package db_lab.data;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOVisitatore {

    // ------------------------------------------------------------------ //
    // INSERT                                                               //
    // ------------------------------------------------------------------ //

    public boolean insert(Visitatore v) throws SQLException {
        String sql = "INSERT INTO VISITATORE (E_Mail, Password, DataCreazione, Nome, Cognome, CodiceFiscale) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getEmail());
            ps.setString(2, v.getPassword());
            ps.setDate(3, Date.valueOf(v.getDataCreazione()));
            ps.setString(4, v.getNome());
            ps.setString(5, v.getCognome());
            ps.setString(6, v.getCodiceFiscale());
            return ps.executeUpdate() > 0;
        }
    }

    // ------------------------------------------------------------------ //
    // SELECT                                                               //
    // ------------------------------------------------------------------ //

    public Visitatore getByID(int accountID) throws SQLException {
        String sql = "SELECT * FROM VISITATORE WHERE AccountID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public Visitatore getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM VISITATORE WHERE E_Mail = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Visitatore> getAll() throws SQLException {
        List<Visitatore> list = new ArrayList<>();
        String sql = "SELECT * FROM VISITATORE";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // ------------------------------------------------------------------ //
    // UPDATE                                                               //
    // ------------------------------------------------------------------ //

    public boolean update(Visitatore v) throws SQLException {
        String sql = "UPDATE VISITATORE SET E_Mail=?, Password=?, Nome=?, Cognome=?, CodiceFiscale=? " +
                     "WHERE AccountID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getEmail());
            ps.setString(2, v.getPassword());
            ps.setString(3, v.getNome());
            ps.setString(4, v.getCognome());
            ps.setString(5, v.getCodiceFiscale());
            ps.setInt(6, v.getAccountID());
            return ps.executeUpdate() > 0;
        }
    }

    // ------------------------------------------------------------------ //
    // DELETE                                                               //
    // ------------------------------------------------------------------ //

    public boolean delete(int accountID) throws SQLException {
        String sql = "DELETE FROM VISITATORE WHERE AccountID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            return ps.executeUpdate() > 0;
        }
    }

    // ------------------------------------------------------------------ //
    // LOGIN                                                                //
    // ------------------------------------------------------------------ //

    public Visitatore login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM VISITATORE WHERE E_Mail = ? AND Password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    // ------------------------------------------------------------------ //
    // MAPPING                                                              //
    // ------------------------------------------------------------------ //

    private Visitatore map(ResultSet rs) throws SQLException {
        return new Visitatore(
            rs.getInt("AccountID"),
            rs.getString("E_Mail"),
            rs.getString("Password"),
            rs.getDate("DataCreazione").toLocalDate(),
            rs.getString("Nome"),
            rs.getString("Cognome"),
            rs.getString("CodiceFiscale")
        );
    }
}