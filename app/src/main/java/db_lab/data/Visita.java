package db_lab.data;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visita {

    public enum EsitoVisita {
        Effettuata,
        Annullata,
        Respinta
    }

    private int numeroVisita;
    private int idPrenotazione;
    private LocalDate data;
    private LocalTime orario;
    private int accountID;
    private EsitoVisita esitoVisita;

    public Visita() {}

    public Visita(int numeroVisita, int idPrenotazione, LocalDate data,
                  LocalTime orario, int accountID, EsitoVisita esitoVisita) {
        this.numeroVisita   = numeroVisita;
        this.idPrenotazione = idPrenotazione;
        this.data           = data;
        this.orario         = orario;
        this.accountID      = accountID;
        this.esitoVisita    = esitoVisita;
    }

    public int getNumeroVisita()                        { return numeroVisita; }
    public void setNumeroVisita(int numeroVisita)        { this.numeroVisita = numeroVisita; }

    public int getIdPrenotazione()                      { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione)   { this.idPrenotazione = idPrenotazione; }

    public LocalDate getData()                          { return data; }
    public void setData(LocalDate data)                 { this.data = data; }

    public LocalTime getOrario()                        { return orario; }
    public void setOrario(LocalTime orario)             { this.orario = orario; }

    public int getAccountID()                           { return accountID; }
    public void setAccountID(int accountID)             { this.accountID = accountID; }

    public EsitoVisita getEsitoVisita()                 { return esitoVisita; }
    public void setEsitoVisita(EsitoVisita esitoVisita) { this.esitoVisita = esitoVisita; }

    @Override
    public String toString() {
        return "Visita{numero=" + numeroVisita + ", data=" + data + ", esito=" + esitoVisita + "}";
    }
}