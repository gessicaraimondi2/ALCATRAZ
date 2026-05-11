package db_lab.model;

import db_lab.data.Personale;
import db_lab.data.Detenuto;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface Model {
    Optional<Personale> find(int productCode);

    List<Detenuto> previews();

    boolean loadedPreviews();

    List<Detenuto> loadPreviews();

    // Create a mocked version of the model.
    //
    static Model mock() {
        return new MockedModel();
    }

    // Create a model that connects to a database using the given connection.
    //
    static Model fromConnection(Connection connection) {
        return new DBModel(connection);
    }
}
