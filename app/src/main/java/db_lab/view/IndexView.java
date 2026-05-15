package db_lab.view;

public class IndexView {

    public void showWelcome() {
        System.out.println("Benvenuto nell'applicazione del carcere.");
    }

    public void showError(String msg) {
        System.out.println("[INDEX ERROR] " + msg);
    }
}
