package db_lab.view;


public class RegisterView {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String codiceFiscale;

    // ----------------------------- //
    // GETTER                        //
    // ----------------------------- //
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    // ----------------------------- //
    // SETTER                        //
    // ----------------------------- //
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    // ----------------------------- //
    // OUTPUT PER IL CONTROLLER      //
    // ----------------------------- //
    public void showError(String msg) {
        System.out.println("[REGISTER ERROR] " + msg);
    }

    public void showSuccess(String msg) {
        System.out.println("[REGISTER OK] " + msg);
    }
}
