package db_lab.data;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class Personale {

    public final int code;
    public final String name;
    public final String description;
    public final Map<Visitatore, Float> composition;

    public Personale(int code, String name, String description, Map<Visitatore, Float> composition) {
        this.code = code;
        this.name = name;
        this.description = description == null ? "" : description;
        this.composition = composition == null ? Map.of() : Collections.unmodifiableMap(new HashMap<>(composition));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Personale) {
            var p = (Personale) other;
            return (
                p.code == this.code &&
                p.name.equals(this.name) &&
                p.description.equals(this.description) &&
                p.composition.equals(this.composition)
            );
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.name, this.description, this.composition);
    }

    @Override
    public String toString() {
        return Amministratore.stringify(
            "Product",
            List.of(
                Amministratore.field("code", this.code),
                Amministratore.field("name", this.name),
                Amministratore.field("description", this.description),
                Amministratore.field("composition", this.composition)
            )
        );
    }

    public static final class DAO {

        public static Optional<Personale> find(Connection connection, int productId) {
            throw new UnsupportedOperationException("Unimplemented");
        }
    }
}
