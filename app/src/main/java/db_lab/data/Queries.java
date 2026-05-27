package db_lab.data;

/**
 * Tutte le query SQL di ALCATRAZ — versione corretta.
 *
 * Ogni query è allineata al metodo DAO che la usa:
 *   - numero di ? == numero di parametri passati in DAOUtils.prepare(...)
 *   - nomi colonne == nomi letti nel metodo map() del DAO corrispondente
 */
public final class Queries {

    // ------------------------------------------------------------------ //
    // AMMINISTRATORI                                                      //
    // ------------------------------------------------------------------ //

    // DAOAmministratore.insert() passa: email, password, nome, cognome, matricola  (5 params)
    // DataCreazione gestita con CURRENT_DATE → solo 5 ? ✓
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

    // DAOAmministratore.update() passa: email, password, nome, cognome, matricola, accountID (6 params)
    public static final String AMMINISTRATORE_UPDATE = """
        UPDATE AMMINISTRATORE
        SET E_Mail=?, Password=?, Nome=?, Cognome=?, Matricola=?
        WHERE AccountID=?
        """;

    public static final String AMMINISTRATORE_DELETE = """
        DELETE FROM AMMINISTRATORE WHERE AccountID = ?
        """;

    // ------------------------------------------------------------------ //
    // VISITATORI                                                          //
    // ------------------------------------------------------------------ //

    // FIX: DAOVisitatore.insert() passa DataCreazione esplicitamente (6 params)
    // → rimosso CURRENT_DATE, aggiunto ? al posto suo
    public static final String VISITATORE_INSERT = """
        INSERT INTO VISITATORE (E_Mail, Password, DataCreazione, Nome, Cognome, CodiceFiscale)
        VALUES (?, ?, ?, ?, ?, ?)
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

    // DAOVisitatore.update() passa: email, password, nome, cognome, codiceFiscale, accountID (6 params)
    public static final String VISITATORE_UPDATE = """
        UPDATE VISITATORE
        SET E_Mail=?, Password=?, Nome=?, Cognome=?, CodiceFiscale=?
        WHERE AccountID=?
        """;

    public static final String VISITATORE_DELETE = """
        DELETE FROM VISITATORE WHERE AccountID = ?
        """;

    // ------------------------------------------------------------------ //
    // DETENUTI                                                            //
    // ------------------------------------------------------------------ //

    // FIX TOTALE: la query originale aveva solo 8 colonne/param.
    // DAODetenuto.insert() passa 12 valori:
    //   matricolaDetenuto, nome, cognome, DataDiNascita, CodiceFiscale,
    //   DataIngresso, DurataPena, Reato, StatoDellaPena, AccountID,
    //   NumeroSezione, NumeroCella
    // I nomi colonna devono corrispondere a quelli letti nel map() del DAO.
    public static final String DETENUTO_INSERT = """
        INSERT INTO DETENUTO
            (MatricolaDetenuto, Nome, Cognome, DataDiNascita, CodiceFiscale,
             DataIngresso, DurataPena, Reato, StatoDellaPena, AccountID,
             NumeroSezione, NumeroCella)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    // FIX: colonna PK è MatricolaDetenuto (non Matricola)
    public static final String DETENUTO_GET_BY_MATRICOLA = """
        SELECT * FROM DETENUTO WHERE MatricolaDetenuto = ?
        """;

    public static final String DETENUTO_GET_ALL = """
        SELECT * FROM DETENUTO
        """;

    // FIX: NumeroSezione / NumeroCella (non Sezione / Cella)
    public static final String DETENUTO_GET_BY_CELLA = """
        SELECT * FROM DETENUTO WHERE NumeroSezione = ? AND NumeroCella = ?
        """;

    public static final String DETENUTO_GET_BY_SEZIONE = """
        SELECT * FROM DETENUTO WHERE NumeroSezione = ?
        """;

    // FIX: DAODetenuto.update() passa 11 valori:
    //   nome, cognome, DataDiNascita, CodiceFiscale, DataIngresso, DurataPena,
    //   Reato, StatoDellaPena, NumeroSezione, NumeroCella, MatricolaDetenuto (WHERE)
    public static final String DETENUTO_UPDATE = """
        UPDATE DETENUTO
        SET Nome=?, Cognome=?, DataDiNascita=?, CodiceFiscale=?, DataIngresso=?,
            DurataPena=?, Reato=?, StatoDellaPena=?, NumeroSezione=?, NumeroCella=?
        WHERE MatricolaDetenuto=?
        """;

    public static final String DETENUTO_DELETE = """
        DELETE FROM DETENUTO WHERE MatricolaDetenuto = ?
        """;

    // ------------------------------------------------------------------ //
    // PERSONALE                                                           //
    // ------------------------------------------------------------------ //

    // FIX: DAOPersonale.insert() passa 6 valori:
    //   matricola, nome, cognome, ruolo, DataAssunzione, AccountID
    // La versione originale aveva "Sezione" al posto di DataAssunzione + AccountID
    public static final String PERSONALE_INSERT = """
        INSERT INTO PERSONALE (Matricola, Nome, Cognome, Ruolo, DataAssunzione, AccountID)
        VALUES (?, ?, ?, ?, ?, ?)
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

    // FIX: DAOPersonale.update() passa 6 valori:
    //   nome, cognome, ruolo, DataAssunzione, AccountID, matricola (WHERE)
    // La versione originale aveva "Sezione" e non aveva DataAssunzione / AccountID
    public static final String PERSONALE_UPDATE = """
        UPDATE PERSONALE
        SET Nome=?, Cognome=?, Ruolo=?, DataAssunzione=?, AccountID=?
        WHERE Matricola=?
        """;

    public static final String PERSONALE_DELETE = """
        DELETE FROM PERSONALE WHERE Matricola = ?
        """;

    // ------------------------------------------------------------------ //
    // PRENOTAZIONI                                                        //
    // ------------------------------------------------------------------ //

    // FIX TOTALE: DAOPrenotazione.insert() passa 7 valori:
    //   numeroAutorizzazione, tipoAutorizzazione, Data, Eff_AccountID,
    //   MatricolaDetenuto, MotivoRifiuto, EsitoPrenotazione
    // La versione originale aveva colonne e parametri completamente diversi.
    public static final String PRENOTAZIONE_INSERT = """
        INSERT INTO PRENOTAZIONE
            (NumeroAutorizzazione, TipoAutorizzazione, Data, Eff_AccountID,
             MatricolaDetenuto, MotivoRifiuto, EsitoPrenotazione)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String PRENOTAZIONE_GET_BY_ID = """
        SELECT * FROM PRENOTAZIONE WHERE IDPrenotazione = ?
        """;

    // FIX: la FK visitatore è Eff_AccountID (non AccountID)
    public static final String PRENOTAZIONE_GET_BY_VISITATORE = """
        SELECT * FROM PRENOTAZIONE WHERE Eff_AccountID = ?
        """;

    public static final String PRENOTAZIONE_GET_BY_DETENUTO = """
        SELECT * FROM PRENOTAZIONE WHERE MatricolaDetenuto = ?
        """;

    // FIX: il valore DB è "In attesa" (con spazio, non 'IN_ATTESA')
    public static final String PRENOTAZIONE_GET_IN_ATTESA = """
        SELECT * FROM PRENOTAZIONE WHERE EsitoPrenotazione = 'In attesa'
        """;

    public static final String PRENOTAZIONE_GET_ALL = """
        SELECT * FROM PRENOTAZIONE
        """;

    // DAOPrenotazione.aggiornaEsito() passa: esito, motivoRifiuto, idPrenotazione (3 params) ✓
    public static final String PRENOTAZIONE_UPDATE_ESITO = """
        UPDATE PRENOTAZIONE SET EsitoPrenotazione=?, MotivoRifiuto=?
        WHERE IDPrenotazione=?
        """;

    public static final String PRENOTAZIONE_DELETE = """
        DELETE FROM PRENOTAZIONE WHERE IDPrenotazione = ?
        """;

    // ------------------------------------------------------------------ //
    // VISITE                                                              //
    // ------------------------------------------------------------------ //

    // FIX: DAOVisita.insert() passa 5 valori:
    //   idPrenotazione, Data (Date), Orario (Time), AccountID, EsitoVisita
    // La versione originale aveva solo 2 ? e colonna unica "DataOra"
    public static final String VISITA_INSERT = """
        INSERT INTO VISITA (IDPrenotazione, Data, Orario, AccountID, EsitoVisita)
        VALUES (?, ?, ?, ?, ?)
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

    // EsitoVisita è salvato con .name() dell'enum → "Effettuata" (case-sensitive)
    public static final String VISITA_GET_EFFETTUATE = """
        SELECT * FROM VISITA WHERE EsitoVisita = 'Effettuata'
        """;

    // DAOVisita.aggiornaEsito() passa: esito, numeroVisita (2 params) ✓
    public static final String VISITA_UPDATE_ESITO = """
        UPDATE VISITA SET EsitoVisita=? WHERE NumeroVisita=?
        """;

    public static final String VISITA_DELETE = """
        DELETE FROM VISITA WHERE NumeroVisita = ?
        """;

    // ------------------------------------------------------------------ //
    // PROVVEDIMENTI                                                       //
    // ------------------------------------------------------------------ //

    // FIX: DAOProvvedimento.insert() passa 5 valori:
    //   tipo, motivazione, DataEmissione, MatricolaDetenuto, Matricola(guardia)
    // La versione originale aveva solo 3 ? e colonna "Descrizione" (non Motivazione)
    public static final String PROVVEDIMENTO_INSERT = """
        INSERT INTO PROVVEDIMENTO_DISCIPLINARE (Tipo, Motivazione, DataEmissione, MatricolaDetenuto, Matricola)
        VALUES (?, ?, ?, ?, ?)
        """;

    public static final String PROVVEDIMENTO_GET_BY_ID = """
        SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE NumeroProv = ?
        """;

    public static final String PROVVEDIMENTO_GET_BY_DETENUTO = """
        SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE MatricolaDetenuto = ?
        """;

    public static final String PROVVEDIMENTO_GET_BY_TIPO = """
        SELECT * FROM PROVVEDIMENTO_DISCIPLINARE WHERE Tipo = ?
        """;

    public static final String PROVVEDIMENTO_GET_ALL = """
        SELECT * FROM PROVVEDIMENTO_DISCIPLINARE
        """;

    public static final String PROVVEDIMENTO_DELETE = """
        DELETE FROM PROVVEDIMENTO_DISCIPLINARE WHERE NumeroProv = ?
        """;

    // ------------------------------------------------------------------ //
    // CORSI                                                               //
    // ------------------------------------------------------------------ //

    // FIX: DAOCorso.insert() passa 7 valori:
    //   titolo, descrizione, DataInizio, DataFine, tipologia, AccountID, Matricola
    // La versione originale aveva 5 ? con "Nome" e "MatricolaEducatore" (nomi errati)
    public static final String CORSO_INSERT = """
        INSERT INTO CORSO_DI_REINSERIMENTO (Titolo, Descrizione, DataInizio, DataFine, Tipologia, AccountID, Matricola)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String CORSO_GET_BY_ID = """
        SELECT * FROM CORSO_DI_REINSERIMENTO WHERE CodiceCorso = ?
        """;

    public static final String CORSO_GET_ALL = """
        SELECT * FROM CORSO_DI_REINSERIMENTO
        """;

    // FIX: la FK educatore si chiama Matricola (non MatricolaEducatore)
    public static final String CORSO_GET_BY_EDUCATORE = """
        SELECT * FROM CORSO_DI_REINSERIMENTO WHERE Matricola = ?
        """;

    // FIX: DAOCorso.update() passa 7 valori:
    //   titolo, descrizione, DataInizio, DataFine, tipologia, Matricola, CodiceCorso (WHERE)
    public static final String CORSO_UPDATE = """
        UPDATE CORSO_DI_REINSERIMENTO
        SET Titolo=?, Descrizione=?, DataInizio=?, DataFine=?, Tipologia=?, Matricola=?
        WHERE CodiceCorso=?
        """;

    public static final String CORSO_DELETE = """
        DELETE FROM CORSO_DI_REINSERIMENTO WHERE CodiceCorso = ?
        """;

    // ------------------------------------------------------------------ //
    // ISCRIZIONI                                                          //
    // ------------------------------------------------------------------ //

    // DAOIscrizione.insert() passa: matricolaDetenuto, codiceCorso (2 params) ✓
    // Esito iniziale impostato a 'In corso' (valore DB dal toDBString())
    public static final String ISCRIZIONE_INSERT = """
        INSERT INTO Iscrizione  (MatricolaDetenuto, CodiceCorso, Esito)
        VALUES (?, ?, 'In corso')
        """;

    public static final String ISCRIZIONE_GET_BY_DETENUTO = """
        SELECT * FROM Iscrizione  WHERE MatricolaDetenuto = ?
        """;

    public static final String ISCRIZIONE_GET_BY_CORSO = """
        SELECT * FROM Iscrizione  WHERE CodiceCorso = ?
        """;

    // DAOIscrizione.updateEsito() passa: esito, matricolaDetenuto, codiceCorso (3 params) ✓
    public static final String ISCRIZIONE_UPDATE_ESITO = """
        UPDATE Iscrizione  SET Esito=? WHERE MatricolaDetenuto=? AND CodiceCorso=?
        """;

    public static final String ISCRIZIONE_DELETE = """
        DELETE FROM Iscrizione  WHERE MatricolaDetenuto=? AND CodiceCorso=?
        """;

    // ------------------------------------------------------------------ //
    // STATISTICHE                                                         //
    // ------------------------------------------------------------------ //

    // FIX: alias corretti per corrispondere a rs.getDouble("TassoPartecipazione_Pct")
    public static final String STAT_TASSO_PARTECIPAZIONE = """
        SELECT
            COUNT(DISTINCT i.MatricolaDetenuto) AS DetenutiIscritti,
            COUNT(DISTINCT d.MatricolaDetenuto) AS TotaleDetenuti,
            ROUND(100.0 * COUNT(DISTINCT i.MatricolaDetenuto)
                / COUNT(DISTINCT d.MatricolaDetenuto), 2) AS TassoPartecipazione_Pct
        FROM DETENUTO d
        LEFT JOIN Iscrizione i
            ON d.MatricolaDetenuto = i.MatricolaDetenuto
        AND i.Esito = 'In corso';
        """;

    // FIX: alias corretto per rs.getDouble("MediaDetenuti_Per_Sezione")
    public static final String STAT_MEDIA_DETENUTI_PER_SEZIONE = """
        SELECT AVG(cnt) AS MediaDetenuti_Per_Sezione
        FROM (SELECT COUNT(*) AS cnt FROM DETENUTO GROUP BY NumeroSezione) t
        """;

    // FIX: alias corretto per rs.getInt("Totale")
    public static final String STAT_PRENOTAZIONI_IN_ATTESA = """
        SELECT COUNT(*) AS Totale 
        FROM PRENOTAZIONE
        WHERE EsitoPrenotazione = 'In attesa'
        """;

    // FIX: colonna StatoDellaPena e alias Totale per rs.getString/getInt nel DAO
    public static final String STAT_DETENUTI_PER_STATO = """
        SELECT StatoDellaPena, COUNT(*) AS Totale FROM DETENUTO GROUP BY StatoDellaPena
        """;
}