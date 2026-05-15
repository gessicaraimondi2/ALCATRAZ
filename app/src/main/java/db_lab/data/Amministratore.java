package db_lab.data;

import java.time.LocalDate;

public class Amministratore {

    private int accountID;
    private String email;
    private String password;
    private LocalDate dataCreazione;
    private String nome;
    private String cognome;
    private String matricola;

    public Amministratore() {}

    public Amministratore(int accountID, String email, String password,
                          LocalDate dataCreazione, String nome, String cognome, String matricola) {
        this.accountID     = accountID;
        this.email         = email;
        this.password      = password;
        this.dataCreazione = dataCreazione;
        this.nome          = nome;
        this.cognome       = cognome;
        this.matricola     = matricola;
    }

    public int getAccountID()                      { return accountID; }
    public void setAccountID(int accountID)         { this.accountID = accountID; }

    public String getEmail()                        { return email; }
    public void setEmail(String email)              { this.email = email; }

    public String getPassword()                     { return password; }
    public void setPassword(String password)        { this.password = password; }

    public LocalDate getDataCreazione()             { return dataCreazione; }
    public void setDataCreazione(LocalDate d)       { this.dataCreazione = d; }

    public String getNome()                         { return nome; }
    public void setNome(String nome)                { this.nome = nome; }

    public String getCognome()                      { return cognome; }
    public void setCognome(String cognome)          { this.cognome = cognome; }

    public String getMatricola()                    { return matricola; }
    public void setMatricola(String matricola)      { this.matricola = matricola; }

    @Override
    public String toString() {
        return "Amministratore{accountID=" + accountID + ", matricola='" + matricola + "', nome='" + nome + "'}";
    }
}