package db_lab.data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Detenuto {

    public final int code;
    public final String name;
    public final Set<Tag> tags;

    public Detenuto(int code, String name, Set<Tag> tags) {
        this.code = code;
        this.name = name == null ? "" : name;
        this.tags = tags == null ? Set.of() : Collections.unmodifiableSet(new HashSet<>(tags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null) {
            return false;
        } else if (other instanceof Detenuto) {
            var p = (Detenuto) other;
            return p.code == this.code && p.name.equals(this.name) && p.tags.equals(this.tags);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.name, this.tags);
    }

    @Override
    public String toString() {
        return Amministratore.stringify(
            "ProductPreview",
            List.of(
                Amministratore.field("code", this.code),
                Amministratore.field("name", this.name),
                Amministratore.field("tags", this.tags)
            )
        );
    }

    public static final class DAO {

        public static List<Detenuto> list(Connection connection) {
            // Iterating through a resultSet:
            // https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
            throw new UnsupportedOperationException("unimplemented");
        }
    }
}
