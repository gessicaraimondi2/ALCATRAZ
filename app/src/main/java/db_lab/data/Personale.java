package db_lab.data;

import java.time.LocalDate;

public class Personale {

    public enum Ruolo { Guardia, Educatore, Amministrativo }

    private String matricola;
    private String nome;
    private String cognome;
    private Ruolo ruolo;
    private LocalDate dataAssunzione;
    private int accountID;

    public Personale() {}

    public Personale(String matricola, String nome, String cognome,
                     Ruolo ruolo, LocalDate dataAssunzione, int accountID) {
        this.matricola      = matricola;
        this.nome           = nome;
        this.cognome        = cognome;
        this.ruolo          = ruolo;
        this.dataAssunzione = dataAssunzione;
        this.accountID      = accountID;
    }

    public String getMatricola()                       { return matricola; }
    public void setMatricola(String matricola)          { this.matricola = matricola; }

    public String getNome()                            { return nome; }
    public void setNome(String nome)                   { this.nome = nome; }

    public String getCognome()                         { return cognome; }
    public void setCognome(String cognome)             { this.cognome = cognome; }

    public Ruolo getRuolo()                            { return ruolo; }
    public void setRuolo(Ruolo ruolo)                  { this.ruolo = ruolo; }

    public LocalDate getDataAssunzione()               { return dataAssunzione; }
    public void setDataAssunzione(LocalDate d)         { this.dataAssunzione = d; }

    public int getAccountID()                          { return accountID; }
    public void setAccountID(int accountID)            { this.accountID = accountID; }

    @Override
    public String toString() {
        return "Personale{matricola='" + matricola + "', nome='" + nome + "', ruolo=" + ruolo + "}";
    }
}