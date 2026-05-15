package db_lab.data;

public class Sorveglia {

    private String matricola;      // FK -> PERSONALE (Guardia), parte della PK
    private String numeroSezione;  // FK -> SEZIONE, parte della PK

    public Sorveglia() {}

    public Sorveglia(String matricola, String numeroSezione) {
        this.matricola     = matricola;
        this.numeroSezione = numeroSezione;
    }

    public String getMatricola()                     { return matricola; }
    public void setMatricola(String matricola)        { this.matricola = matricola; }

    public String getNumeroSezione()                 { return numeroSezione; }
    public void setNumeroSezione(String numeroSezione){ this.numeroSezione = numeroSezione; }

    @Override
    public String toString() {
        return "Sorveglia{matricola='" + matricola + "', sezione='" + numeroSezione + "'}";
    }
}
