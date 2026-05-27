package db_lab.data;

/**
 * Eccezione unchecked per i DAO.
 *
 * Invece di propagare SQLException (checked) attraverso tutta la catena
 * Controller → Model → DAO, la avvolgiamo qui. I Controller la gestiscono
 * con un semplice try/catch(DAOException e).
 */
public class DAOException extends RuntimeException {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}