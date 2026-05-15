package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOVisita {

    public boolean insert(Visita v) throws SQLException {
        String sql = "INSERT INTO VISITA (IDPrenotazione, Data, Orario, AccountID, EsitoVisita) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getIdPrenotazione());
            ps.setDate(2, Date.valueOf(v.getData()));
            ps.setTime(3, Time.valueOf(v.getOrario()));
            ps.setInt(4, v.getAccountID());
            ps.setString(5, v.getEsitoVisita().name());
            return ps.executeUpdate() > 0;
        }
    }

    public Visita getByID(int numeroVisita) throws SQLException {
        String sql = "SELECT * FROM VISITA WHERE NumeroVisita = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroVisita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public Visita getByPrenotazione(int idPrenotazione) throws SQLException {
        String sql = "SELECT * FROM VISITA WHERE IDPrenotazione = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrenotazione);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Visita> getAll() throws SQLException {
        List<Visita> list = new ArrayList<>();
        String sql = "SELECT * FROM VISITA";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Visita> getEffettuate() throws SQLException {
        List<Visita> list = new ArrayList<>();
        String sql = "SELECT * FROM VISITA WHERE EsitoVisita = 'Effettuata'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean aggiornaEsito(int numeroVisita, Visita.EsitoVisita esito) throws SQLException {
        String sql = "UPDATE VISITA SET EsitoVisita=? WHERE NumeroVisita=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, esito.name());
            ps.setInt(2, numeroVisita);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int numeroVisita) throws SQLException {
        String sql = "DELETE FROM VISITA WHERE NumeroVisita = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroVisita);
            return ps.executeUpdate() > 0;
        }
    }

    private Visita map(ResultSet rs) throws SQLException {
        return new Visita(
            rs.getInt("NumeroVisita"),
            rs.getInt("IDPrenotazione"),
            rs.getDate("Data").toLocalDate(),
            rs.getTime("Orario").toLocalTime(),
            rs.getInt("AccountID"),
            Visita.EsitoVisita.valueOf(rs.getString("EsitoVisita"))
        );
    }
}