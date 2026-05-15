package db_lab.view;

public class StatisticheView {

    public void showTassoPartecipazione(double tasso) {
        System.out.println("Tasso partecipazione corsi: " + tasso + "%");
    }

    public void showMediaDetenuti(double media) {
        System.out.println("Media detenuti per sezione: " + media);
    }

    public void showPrenotazioniInAttesa(int totale) {
        System.out.println("Prenotazioni in attesa: " + totale);
    }

    public void showMessage(String msg) {
        System.out.println("[STATISTICHE] " + msg);
    }

    public void showError(String msg) {
        System.out.println("[STATISTICHE ERROR] " + msg);
    }
    
}
