package db_lab.data;

import java.time.LocalDate;

public class Corso {

    public enum Tipologia {
        Professionale,
        Scolastico,
        Ricreativo
    }

    private int codiceCorso;
    private String titolo;
    private String descrizione;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Tipologia tipologia;
    private int accountID;
    private String matricola;   // FK -> PERSONALE (Educatore)

    public Corso() {}

    public Corso(int codiceCorso, String titolo, String descrizione,
                 LocalDate dataInizio, LocalDate dataFine,
                 Tipologia tipologia, int accountID, String matricola) {
        this.codiceCorso  = codiceCorso;
        this.titolo       = titolo;
        this.descrizione  = descrizione;
        this.dataInizio   = dataInizio;
        this.dataFine     = dataFine;
        this.tipologia    = tipologia;
        this.accountID    = accountID;
        this.matricola    = matricola;
    }

    public int getCodiceCorso()                        { return codiceCorso; }
    public void setCodiceCorso(int codiceCorso)         { this.codiceCorso = codiceCorso; }

    public String getTitolo()                          { return titolo; }
    public void setTitolo(String titolo)               { this.titolo = titolo; }

    public String getDescrizione()                     { return descrizione; }
    public void setDescrizione(String descrizione)     { this.descrizione = descrizione; }

    public LocalDate getDataInizio()                   { return dataInizio; }
    public void setDataInizio(LocalDate d)             { this.dataInizio = d; }

    public LocalDate getDataFine()                     { return dataFine; }
    public void setDataFine(LocalDate d)               { this.dataFine = d; }

    public Tipologia getTipologia()                    { return tipologia; }
    public void setTipologia(Tipologia tipologia)      { this.tipologia = tipologia; }

    public int getAccountID()                          { return accountID; }
    public void setAccountID(int accountID)            { this.accountID = accountID; }

    public String getMatricola()                       { return matricola; }
    public void setMatricola(String matricola)         { this.matricola = matricola; }

    @Override
    public String toString() {
        return "Corso{codice=" + codiceCorso + ", titolo='" + titolo + "', tipologia=" + tipologia + "}";
    }
}
