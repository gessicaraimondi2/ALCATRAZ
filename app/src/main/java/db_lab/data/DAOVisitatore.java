package db_lab.data;

import java.io.Serial;

// This is a runtime exception we define to wrap all the exceptions coming from
// the DAO objects we're going to define.
//
// This way we won't have `SQLException`s bubbling up in all other functions.
//
public final class DAOVisitatore extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DAOVisitatore(String message) {
        super(message);
    }

    public DAOVisitatore(Throwable cause) {
        super(cause);
    }

    public DAOVisitatore(String message, Throwable cause) {
        super(message, cause);
    }
}
