package db_lab.data;

import java.time.LocalDate;

public class Detenuto {

    public enum StatoDellaPena {
        In_corso,
        Scontata,
        In_attesa_di_giudizio;

        public String toDBString() {
            return switch (this) {
                case In_corso              -> "In corso";
                case Scontata              -> "Scontata";
                case In_attesa_di_giudizio -> "In attesa di giudizio";
            };
        }
    }

    private String matricolaDetenuto;
    private String nome;
    private String cognome;
    private LocalDate dataDiNascita;
    private String codiceFiscale;
    private LocalDate dataIngresso;
    private String durataPena;
    private String reato;
    private StatoDellaPena statoDellaPena;
    private int accountID;
    private String numeroSezione;
    private String numeroCella;

    public Detenuto() {}

    public Detenuto(String matricolaDetenuto, String nome, String cognome,
                    LocalDate dataDiNascita, String codiceFiscale, LocalDate dataIngresso,
                    String durataPena, String reato, StatoDellaPena statoDellaPena,
                    int accountID, String numeroSezione, String numeroCella) {
        this.matricolaDetenuto = matricolaDetenuto;
        this.nome              = nome;
        this.cognome           = cognome;
        this.dataDiNascita     = dataDiNascita;
        this.codiceFiscale     = codiceFiscale;
        this.dataIngresso      = dataIngresso;
        this.durataPena        = durataPena;
        this.reato             = reato;
        this.statoDellaPena    = statoDellaPena;
        this.accountID         = accountID;
        this.numeroSezione     = numeroSezione;
        this.numeroCella       = numeroCella;
    }

    public String getMatricolaDetenuto()                         { return matricolaDetenuto; }
    public void setMatricolaDetenuto(String matricolaDetenuto)   { this.matricolaDetenuto = matricolaDetenuto; }

    public String getNome()                                      { return nome; }
    public void setNome(String nome)                             { this.nome = nome; }

    public String getCognome()                                   { return cognome; }
    public void setCognome(String cognome)                       { this.cognome = cognome; }

    public LocalDate getDataDiNascita()                          { return dataDiNascita; }
    public void setDataDiNascita(LocalDate d)                    { this.dataDiNascita = d; }

    public String getCodiceFiscale()                             { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale)           { this.codiceFiscale = codiceFiscale; }

    public LocalDate getDataIngresso()                           { return dataIngresso; }
    public void setDataIngresso(LocalDate d)                     { this.dataIngresso = d; }

    public String getDurataPena()                                { return durataPena; }
    public void setDurataPena(String durataPena)                 { this.durataPena = durataPena; }

    public String getReato()                                     { return reato; }
    public void setReato(String reato)                           { this.reato = reato; }

    public StatoDellaPena getStatoDellaPena()                    { return statoDellaPena; }
    public void setStatoDellaPena(StatoDellaPena s)              { this.statoDellaPena = s; }

    public int getAccountID()                                    { return accountID; }
    public void setAccountID(int accountID)                      { this.accountID = accountID; }

    public String getNumeroSezione()                             { return numeroSezione; }
    public void setNumeroSezione(String numeroSezione)           { this.numeroSezione = numeroSezione; }

    public String getNumeroCella()                               { return numeroCella; }
    public void setNumeroCella(String numeroCella)               { this.numeroCella = numeroCella; }

    @Override
    public String toString() {
        return "Detenuto{matricola='" + matricolaDetenuto + "', nome='" + nome + "', stato=" + statoDellaPena + "}";
    }
}