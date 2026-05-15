package db_lab.view;

import db_lab.data.*;

public class DetenutoDettaglioView {

    public void showDetenuto(Detenuto d) {
        System.out.println("=== DETTAGLI DETENUTO ===");
        System.out.println(d);
    }

    public void showProvvedimenti(java.util.List<Provvedimento> provvedimenti) {
        System.out.println("=== PROVVEDIMENTI ===");
        provvedimenti.forEach(System.out::println);
    }

    public void showMessage(String msg) {
        System.out.println("[DETENUTO] " + msg);
    }

    public void showError(String msg) {
        System.out.println("[DETENUTO ERROR] " + msg);
    }
    
}
