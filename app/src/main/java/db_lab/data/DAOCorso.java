package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCorso {

    public boolean insert(Corso c) throws SQLException {
        String sql = "INSERT INTO CORSO_DI_REINSERIMENTO (Titolo, Descrizione, DataInizio, DataFine, " +
                     "Tipologia, AccountID, Matricola) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTitolo());
            ps.setString(2, c.getDescrizione());
            ps.setDate(3, Date.valueOf(c.getDataInizio()));
            ps.setDate(4, Date.valueOf(c.getDataFine()));
            ps.setString(5, c.getTipologia().name());
            ps.setInt(6, c.getAccountID());
            ps.setString(7, c.getMatricola());
            return ps.executeUpdate() > 0;
        }
    }

    public Corso getByCodice(int codiceCorso) throws SQLException {
        String sql = "SELECT * FROM CORSO_DI_REINSERIMENTO WHERE CodiceCorso = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codiceCorso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Corso> getAll() throws SQLException {
        List<Corso> list = new ArrayList<>();
        String sql = "SELECT * FROM CORSO_DI_REINSERIMENTO";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Corso> getByEducatore(String matricola) throws SQLException {
        List<Corso> list = new ArrayList<>();
        String sql = "SELECT * FROM CORSO_DI_REINSERIMENTO WHERE Matricola = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricola);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean update(Corso c) throws SQLException {
        String sql = "UPDATE CORSO_DI_REINSERIMENTO SET Titolo=?, Descrizione=?, DataInizio=?, " +
                     "DataFine=?, Tipologia=?, Matricola=? WHERE CodiceCorso=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTitolo());
            ps.setString(2, c.getDescrizione());
            ps.setDate(3, Date.valueOf(c.getDataInizio()));
            ps.setDate(4, Date.valueOf(c.getDataFine()));
            ps.setString(5, c.getTipologia().name());
            ps.setString(6, c.getMatricola());
            ps.setInt(7, c.getCodiceCorso());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int codiceCorso) throws SQLException {
        String sql = "DELETE FROM CORSO_DI_REINSERIMENTO WHERE CodiceCorso = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codiceCorso);
            return ps.executeUpdate() > 0;
        }
    }

    private Corso map(ResultSet rs) throws SQLException {
        return new Corso(
            rs.getInt("CodiceCorso"),
            rs.getString("Titolo"),
            rs.getString("Descrizione"),
            rs.getDate("DataInizio").toLocalDate(),
            rs.getDate("DataFine").toLocalDate(),
            Corso.Tipologia.valueOf(rs.getString("Tipologia")),
            rs.getInt("AccountID"),
            rs.getString("Matricola")
        );
    }
}
