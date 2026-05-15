package db_lab.data;

import java.time.LocalDate;

public class Visitatore {

    private int accountID;
    private String email;
    private String password;
    private LocalDate dataCreazione;
    private String nome;
    private String cognome;
    private String codiceFiscale;

    public Visitatore() {}

    public Visitatore(int accountID, String email, String password,
                      LocalDate dataCreazione, String nome, String cognome, String codiceFiscale) {
        this.accountID     = accountID;
        this.email         = email;
        this.password      = password;
        this.dataCreazione = dataCreazione;
        this.nome          = nome;
        this.cognome       = cognome;
        this.codiceFiscale = codiceFiscale;
    }

    // Getters e Setters
    public int getAccountID()                     { return accountID; }
    public void setAccountID(int accountID)        { this.accountID = accountID; }

    public String getEmail()                       { return email; }
    public void setEmail(String email)             { this.email = email; }

    public String getPassword()                    { return password; }
    public void setPassword(String password)       { this.password = password; }

    public LocalDate getDataCreazione()            { return dataCreazione; }
    public void setDataCreazione(LocalDate d)      { this.dataCreazione = d; }

    public String getNome()                        { return nome; }
    public void setNome(String nome)               { this.nome = nome; }

    public String getCognome()                     { return cognome; }
    public void setCognome(String cognome)         { this.cognome = cognome; }

    public String getCodiceFiscale()               { return codiceFiscale; }
    public void setCodiceFiscale(String cf)        { this.codiceFiscale = cf; }

    @Override
    public String toString() {
        return "Visitatore{accountID=" + accountID + ", nome='" + nome + "', cognome='" + cognome + "'}";
    }
}