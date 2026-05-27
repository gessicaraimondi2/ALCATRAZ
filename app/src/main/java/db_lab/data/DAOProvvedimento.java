package db_lab.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOProvvedimento {

    private final Connection connection;

    public DAOProvvedimento(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Provvedimento p) {
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_INSERT,
                p.getTipo().name(), p.getMotivazione(),
                Date.valueOf(p.getDataEmissione()),
                p.getMatricolaDetenuto(), p.getMatricola())) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore insert provvedimento", e);
        }
    }

    public Provvedimento getByID(int numeroProv) {
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_GET_BY_ID, numeroProv);
             var rs = ps.executeQuery()) {
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new DAOException("Errore getByID provvedimento", e);
        }
        return null;
    }

    public List<Provvedimento> getByDetenuto(String matricolaDetenuto) {
        var list = new ArrayList<Provvedimento>();
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_GET_BY_DETENUTO, matricolaDetenuto);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByDetenuto provvedimento", e);
        }
        return list;
    }

    public List<Provvedimento> getByTipo(Provvedimento.Tipo tipo) {
        var list = new ArrayList<Provvedimento>();
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_GET_BY_TIPO, tipo.name());
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getByTipo provvedimento", e);
        }
        return list;
    }

    public List<Provvedimento> getAll() {
        var list = new ArrayList<Provvedimento>();
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_GET_ALL);
             var rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Errore getAll provvedimenti", e);
        }
        return list;
    }

    public boolean delete(int numeroProv) {
        try (var ps = DAOUtils.prepare(connection, Queries.PROVVEDIMENTO_DELETE, numeroProv)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Errore delete provvedimento", e);
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