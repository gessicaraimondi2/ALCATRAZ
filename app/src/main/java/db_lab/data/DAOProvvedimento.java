package db_lab.data;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOProvvedimento {

    public boolean insert(Provvedimento p) throws SQLException {
        String sql = "INSERT INTO PROVVEDIMENTO_DISCIPLINARE (Tipo, Motivazione, DataEmissione, " +
                     "MatricolaDetenuto, Matricola) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTipo().name());
            ps.setString(2, p.getMotivazione());
            ps.setDate(3, Date.valueOf(p.getDataEmissione()));
            ps.setString(4, p.getMatricolaDetenuto());
            ps.setString(5, p.getMatricola());
            return ps.executeUpdate() > 0;
        }
    }

    public Provvedimento getByID(int numeroProv) throws SQLException {
        String sql = "SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE NumeroProv = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroProv);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        }
        return null;
    }

    public List<Provvedimento> getByDetenuto(String matricolaDetenuto) throws SQLException {
        List<Provvedimento> list = new ArrayList<>();
        String sql = "SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE MatricolaDetenuto = ? ORDER BY DataEmissione DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricolaDetenuto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Provvedimento> getByTipo(Provvedimento.Tipo tipo) throws SQLException {
        List<Provvedimento> list = new ArrayList<>();
        String sql = "SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE Tipo = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Provvedimento> getAll() throws SQLException {
        List<Provvedimento> list = new ArrayList<>();
        String sql = "SELECT * FROM PROVVEDIMENTO_DISCIPLINARE ORDER BY DataEmissione DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean delete(int numeroProv) throws SQLException {
        String sql = "DELETE FROM PROVVEDIMENTO_DISCIPLINARE WHERE NumeroProv = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroProv);
            return ps.executeUpdate() > 0;
        }
    }

    private Provvedimento map(ResultSet rs) throws SQLException {
        return new Provvedimento(
            rs.getInt("NumeroProv"),
            Provvedimento.Tipo.valueOf(rs.getString("Tipo")),
            rs.getString("Motivazione"),
            rs.getDate("DataEmissione").toLocalDate(),
            rs.getString("MatricolaDetenuto"),
            rs.getString("Matricola")
        );
    }
}
