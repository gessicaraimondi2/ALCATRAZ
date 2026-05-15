package db_lab.view;

import db_lab.data.Prenotazione;
import java.time.LocalDate;

public class PrenotazioneView {

    private int numeroAutorizzazione;
    private String tipoAutorizzazione;
    private LocalDate data;
    private int effAccountID;
    private String matricolaDetenuto;

    // GETTER
    public int getNumeroAutorizzazione() { return numeroAutorizzazione; }
    public String getTipoAutorizzazione() { return tipoAutorizzazione; }
    public LocalDate getData() { return data; }
    public int getEffAccountID() { return effAccountID; }
    public String getMatricolaDetenuto() { return matricolaDetenuto; }

    // SETTER
    public void setNumeroAutorizzazione(int n) { this.numeroAutorizzazione = n; }
    public void setTipoAutorizzazione(String t) { this.tipoAutorizzazione = t; }
    public void setData(LocalDate d) { this.data = d; }
    public void setEffAccountID(int id) { this.effAccountID = id; }
    public void setMatricolaDetenuto(String m) { this.matricolaDetenuto = m; }

    public void showSuccess(String msg) {
        System.out.println("[PRENOTAZIONE OK] " + msg);
    }

    public void showError(String msg) {
        System.out.println("[PRENOTAZIONE ERROR] " + msg);
    }

}
