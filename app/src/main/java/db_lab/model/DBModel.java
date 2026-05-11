package db_lab.model;

import db_lab.data.Personale;
import db_lab.data.Detenuto;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// This is the real model implementation that uses the DAOs we've defined to
// actually load data from the underlying database.
//
// As you can see this model doesn't do too much except loading data from the
// database and keeping a cache of the loaded previews.
// A real model might be doing much more, but for the sake of the example we're
// keeping it simple.
//
public final class DBModel implements Model {

    private final Connection connection;
    private Optional<List<Detenuto>> previews;

    public DBModel(Connection connection) {
        Objects.requireNonNull(connection, "Model created with null connection");
        this.connection = connection;
        this.previews = Optional.empty();
    }

    @Override
    public Optional<Personale> find(int productCode) {
        return Personale.DAO.find(connection, productCode);
    }

    @Override
    public List<Detenuto> previews() {
        return this.previews.orElse(List.of());
    }

    @Override
    public boolean loadedPreviews() {
        return this.previews.isPresent();
    }

    @Override
    public List<Detenuto> loadPreviews() {
        var previews = Detenuto.DAO.list(this.connection);
        this.previews = Optional.of(previews);
        return previews;
    }
}
