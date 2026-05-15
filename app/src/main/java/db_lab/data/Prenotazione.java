package db_lab.data;

import java.time.LocalDate;

public class Prenotazione {

    public enum EsitoPrenotazione {
        In_attesa,
        Confermata,
        Rifiutata;

        public String toDBString() {
            return switch (this) {
                case In_attesa  -> "In attesa";
                case Confermata -> "Confermata";
                case Rifiutata  -> "Rifiutata";
            };
        }
    }

    private int idPrenotazione;
    private int numeroAutorizzazione;
    private String tipoAutorizzazione;
    private LocalDate data;
    private int effAccountID;
    private String matricolaDetenuto;
    private String motivoRifiuto;       // nullable
    private EsitoPrenotazione esitoPrenotazione;

    public Prenotazione() {}

    public Prenotazione(int idPrenotazione, int numeroAutorizzazione, String tipoAutorizzazione,
                        LocalDate data, int effAccountID, String matricolaDetenuto,
                        String motivoRifiuto, EsitoPrenotazione esitoPrenotazione) {
        this.idPrenotazione       = idPrenotazione;
        this.numeroAutorizzazione = numeroAutorizzazione;
        this.tipoAutorizzazione   = tipoAutorizzazione;
        this.data                 = data;
        this.effAccountID         = effAccountID;
        this.matricolaDetenuto    = matricolaDetenuto;
        this.motivoRifiuto        = motivoRifiuto;
        this.esitoPrenotazione    = esitoPrenotazione;
    }

    public int getIdPrenotazione()                              { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione)           { this.idPrenotazione = idPrenotazione; }

    public int getNumeroAutorizzazione()                        { return numeroAutorizzazione; }
    public void setNumeroAutorizzazione(int n)                  { this.numeroAutorizzazione = n; }

    public String getTipoAutorizzazione()                       { return tipoAutorizzazione; }
    public void setTipoAutorizzazione(String tipoAutorizzazione){ this.tipoAutorizzazione = tipoAutorizzazione; }

    public LocalDate getData()                                  { return data; }
    public void setData(LocalDate data)                         { this.data = data; }

    public int getEffAccountID()                                { return effAccountID; }
    public void setEffAccountID(int effAccountID)               { this.effAccountID = effAccountID; }

    public String getMatricolaDetenuto()                        { return matricolaDetenuto; }
    public void setMatricolaDetenuto(String m)                  { this.matricolaDetenuto = m; }

    public String getMotivoRifiuto()                            { return motivoRifiuto; }
    public void setMotivoRifiuto(String motivoRifiuto)          { this.motivoRifiuto = motivoRifiuto; }

    public EsitoPrenotazione getEsitoPrenotazione()             { return esitoPrenotazione; }
    public void setEsitoPrenotazione(EsitoPrenotazione e)       { this.esitoPrenotazione = e; }

    @Override
    public String toString() {
        return "Prenotazione{id=" + idPrenotazione + ", esito=" + esitoPrenotazione + "}";
    }
}