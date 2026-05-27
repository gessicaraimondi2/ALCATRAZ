package db_lab.data;

/**
 * Tutte le query SQL di ALCATRAZ in costanti.
 *
 * Vantaggi:
 *   - Le SQL sono in un posto solo — facile trovarle e modificarle
 *   - I DAO diventano solo "esecutori" senza SQL inline
 *   - Errori di sintassi SQL si trovano subito invece che a runtime
 */
public final class Queries {

    // ------------------------------------------------------------------ //
    // AMMINISTRATORI                                                      //
    // ------------------------------------------------------------------ //

    public static final String AMMINISTRATORE_INSERT = """
        INSERT INTO AMMINISTRATORE (E_Mail, Password, DataCreazione, Nome, Cognome, Matricola)
        VALUES (?, ?, CURRENT_DATE, ?, ?, ?)
        """;

    public static final String AMMINISTRATORE_GET_BY_ID = """
        SELECT * FROM AMMINISTRATORE WHERE AccountID = ?
        """;

    public static final String AMMINISTRATORE_GET_ALL = """
        SELECT * FROM AMMINISTRATORE
        """;

    public static final String AMMINISTRATORE_LOGIN = """
        SELECT * FROM AMMINISTRATORE WHERE E_Mail = ? AND Password = ?
        """;

    public static final String AMMINISTRATORE_UPDATE = """
        UPDATE AMMINISTRATORE
        SET E_Mail=?, Password=?, Nome=?, Cognome=?, Matricola=?
        WHERE AccountID=?
        """;

    public static final String AMMINISTRATORE_DELETE = """
        DELETE FROM AMMINISTRATORE WHERE AccountID = ?
        """;

    // ------------------------------------------------------------------ //
    // VISITATORI                                                         //
    // ------------------------------------------------------------------ //

    public static final String VISITATORE_INSERT = """
        INSERT INTO VISITATORE (E_Mail, Password, DataCreazione, Nome, Cognome, CodiceFiscale)
        VALUES (?, ?, CURRENT_DATE, ?, ?, ?)
        """;

    public static final String VISITATORE_GET_BY_ID = """
        SELECT * FROM VISITATORE WHERE AccountID = ?
        """;

    public static final String VISITATORE_GET_BY_EMAIL = """
        SELECT * FROM VISITATORE WHERE E_Mail = ?
        """;

    public static final String VISITATORE_GET_ALL = """
        SELECT * FROM VISITATORE
        """;

    public static final String VISITATORE_LOGIN = """
        SELECT * FROM VISITATORE WHERE E_Mail = ? AND Password = ?
        """;

    public static final String VISITATORE_UPDATE = """
        UPDATE VISITATORE SET E_Mail=?, Password=?, Nome=?, Cognome=?, CodiceFiscale=?
        WHERE AccountID=?
        """;

    public static final String VISITATORE_DELETE = """
        DELETE FROM VISITATORE WHERE AccountID = ?
        """;

    // ------------------------------------------------------------------ //
    // DETENUTI                                                           //
    // ------------------------------------------------------------------ //

    public static final String DETENUTO_INSERT = """
        INSERT INTO DETENUTO (Matricola, Nome, Cognome, DataNascita, Reato, Sezione, Cella, Stato)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String DETENUTO_GET_BY_MATRICOLA = """
        SELECT * FROM DETENUTO WHERE Matricola = ?
        """;

    public static final String DETENUTO_GET_ALL = """
        SELECT * FROM DETENUTO
        """;

    public static final String DETENUTO_GET_BY_CELLA = """
        SELECT * FROM DETENUTO WHERE Sezione = ? AND Cella = ?
        """;

    public static final String DETENUTO_GET_BY_SEZIONE = """
        SELECT * FROM DETENUTO WHERE Sezione = ?
        """;

    public static final String DETENUTO_UPDATE = """
        UPDATE DETENUTO SET Nome=?, Cognome=?, DataNascita=?, Reato=?, Sezione=?, Cella=?, Stato=?
        WHERE Matricola=?
        """;

    public static final String DETENUTO_DELETE = """
        DELETE FROM DETENUTO WHERE Matricola = ?
        """;

    // ------------------------------------------------------------------ //
    // PERSONALE                                                          //
    // ------------------------------------------------------------------ //

    public static final String PERSONALE_INSERT = """
        INSERT INTO PERSONALE (Matricola, Nome, Cognome, Ruolo, Sezione)
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String PERSONALE_GET_BY_MATRICOLA = """
        SELECT * FROM PERSONALE WHERE Matricola = ?
        """;

    public static final String PERSONALE_GET_ALL = """
        SELECT * FROM PERSONALE
        """;

    public static final String PERSONALE_GET_BY_RUOLO = """
        SELECT * FROM PERSONALE WHERE Ruolo = ?
        """;

    public static final String PERSONALE_UPDATE = """
        UPDATE PERSONALE SET Nome=?, Cognome=?, Ruolo=?, Sezione=? WHERE Matricola=?
        """;

    public static final String PERSONALE_DELETE = """
        DELETE FROM PERSONALE WHERE Matricola = ?
        """;

    // ------------------------------------------------------------------ //
    // PRENOTAZIONI                                                       //
    // ------------------------------------------------------------------ //

    public static final String PRENOTAZIONE_INSERT = """
        INSERT INTO PRENOTAZIONE (AccountID, MatricolaDetenuto, DataRichiesta, DataVisita, Esito, Motivo)
        VALUES (?, ?, CURRENT_DATE, ?, 'IN_ATTESA', NULL)
        """;

    public static final String PRENOTAZIONE_GET_BY_ID = """
        SELECT * FROM PRENOTAZIONE WHERE IDPrenotazione = ?
        """;

    public static final String PRENOTAZIONE_GET_BY_VISITATORE = """
        SELECT * FROM PRENOTAZIONE WHERE AccountID = ?
        """;

    public static final String PRENOTAZIONE_GET_BY_DETENUTO = """
        SELECT * FROM PRENOTAZIONE WHERE MatricolaDetenuto = ?
        """;

    public static final String PRENOTAZIONE_GET_IN_ATTESA = """
        SELECT * FROM PRENOTAZIONE WHERE Esito = 'IN_ATTESA'
        """;

    public static final String PRENOTAZIONE_GET_ALL = """
        SELECT * FROM PRENOTAZIONE
        """;

    public static final String PRENOTAZIONE_UPDATE_ESITO = """
        UPDATE PRENOTAZIONE SET Esito=?, Motivo=? WHERE IDPrenotazione=?
        """;

    public static final String PRENOTAZIONE_DELETE = """
        DELETE FROM PRENOTAZIONE WHERE IDPrenotazione = ?
        """;

    // ------------------------------------------------------------------ //
    // VISITE                                                             //
    // ------------------------------------------------------------------ //

    public static final String VISITA_INSERT = """
        INSERT INTO VISITA (IDPrenotazione, DataOra, Esito)
        VALUES (?, ?, 'IN_CORSO')
        """;

    public static final String VISITA_GET_BY_ID = """
        SELECT * FROM VISITA WHERE NumeroVisita = ?
        """;

    public static final String VISITA_GET_BY_PRENOTAZIONE = """
        SELECT * FROM VISITA WHERE IDPrenotazione = ?
        """;

    public static final String VISITA_GET_ALL = """
        SELECT * FROM VISITA
        """;

    public static final String VISITA_GET_EFFETTUATE = """
        SELECT * FROM VISITA WHERE Esito = 'EFFETTUATA'
        """;

    public static final String VISITA_UPDATE_ESITO = """
        UPDATE VISITA SET Esito=? WHERE NumeroVisita=?
        """;

    public static final String VISITA_DELETE = """
        DELETE FROM VISITA WHERE NumeroVisita = ?
        """;

    // ------------------------------------------------------------------ //
    // PROVVEDIMENTI                                                      //
    // ------------------------------------------------------------------ //

    public static final String PROVVEDIMENTO_INSERT = """
        INSERT INTO PROVVEDIMENTO (MatricolaDetenuto, Tipo, Descrizione, Data)
        VALUES (?, ?, ?, CURRENT_DATE)
        """;

    public static final String PROVVEDIMENTO_GET_BY_ID = """
        SELECT * FROM PROVVEDIMENTO WHERE NumeroProv = ?
        """;

    public static final String PROVVEDIMENTO_GET_BY_DETENUTO = """
        SELECT * FROM PROVVEDIMENTO WHERE MatricolaDetenuto = ?
        """;

    public static final String PROVVEDIMENTO_GET_BY_TIPO = """
        SELECT * FROM PROVVEDIMENTO WHERE Tipo = ?
        """;

    public static final String PROVVEDIMENTO_GET_ALL = """
        SELECT * FROM PROVVEDIMENTO
        """;

    public static final String PROVVEDIMENTO_DELETE = """
        DELETE FROM PROVVEDIMENTO WHERE NumeroProv = ?
        """;

    // ------------------------------------------------------------------ //
    // CORSI                                                              //
    // ------------------------------------------------------------------ //

    public static final String CORSO_INSERT = """
        INSERT INTO CORSO (Nome, Descrizione, MatricolaEducatore, DataInizio, DataFine)
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String CORSO_GET_BY_ID = """
        SELECT * FROM CORSO WHERE CodiceCorso = ?
        """;

    public static final String CORSO_GET_ALL = """
        SELECT * FROM CORSO
        """;

    public static final String CORSO_GET_BY_EDUCATORE = """
        SELECT * FROM CORSO WHERE MatricolaEducatore = ?
        """;

    public static final String CORSO_UPDATE = """
        UPDATE CORSO SET Nome=?, Descrizione=?, MatricolaEducatore=?, DataInizio=?, DataFine=?
        WHERE CodiceCorso=?
        """;

    public static final String CORSO_DELETE = """
        DELETE FROM CORSO WHERE CodiceCorso = ?
        """;

    // ------------------------------------------------------------------ //
    // ISCRIZIONI                                                         //
    // ------------------------------------------------------------------ //

    public static final String ISCRIZIONE_INSERT = """
        INSERT INTO ISCRIZIONE (MatricolaDetenuto, CodiceCorso, Esito)
        VALUES (?, ?, 'IN_CORSO')
        """;

    public static final String ISCRIZIONE_GET_BY_DETENUTO = """
        SELECT * FROM ISCRIZIONE WHERE MatricolaDetenuto = ?
        """;

    public static final String ISCRIZIONE_GET_BY_CORSO = """
        SELECT * FROM ISCRIZIONE WHERE CodiceCorso = ?
        """;

    public static final String ISCRIZIONE_UPDATE_ESITO = """
        UPDATE ISCRIZIONE SET Esito=? WHERE MatricolaDetenuto=? AND CodiceCorso=?
        """;

    public static final String ISCRIZIONE_DELETE = """
        DELETE FROM ISCRIZIONE WHERE MatricolaDetenuto=? AND CodiceCorso=?
        """;

    // ------------------------------------------------------------------ //
    // STATISTICHE                                                        //
    // ------------------------------------------------------------------ //

    public static final String STAT_TASSO_PARTECIPAZIONE = """
        SELECT COUNT(*) * 1.0 / (SELECT COUNT(*) FROM DETENUTO) FROM ISCRIZIONE
        WHERE Esito != 'RITIRATO'
        """;

    public static final String STAT_MEDIA_DETENUTI_PER_SEZIONE = """
        SELECT AVG(cnt) FROM (
            SELECT COUNT(*) AS cnt FROM DETENUTO GROUP BY Sezione
        ) t
        """;

    public static final String STAT_PRENOTAZIONI_IN_ATTESA = """
        SELECT COUNT(*) FROM PRENOTAZIONE WHERE Esito = 'IN_ATTESA'
        """;

    public static final String STAT_DETENUTI_PER_STATO = """
        SELECT Stato, COUNT(*) AS totale FROM DETENUTO GROUP BY Stato
        """;
}