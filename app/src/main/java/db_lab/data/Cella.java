package db_lab.data;

public class Cella {

    private String numeroSezione;
    private String numeroCella;
    private int capienzaMax;

    public Cella() {}

    public Cella(String numeroSezione, String numeroCella, int capienzaMax) {
        this.numeroSezione = numeroSezione;
        this.numeroCella   = numeroCella;
        this.capienzaMax   = capienzaMax;
    }

    public String getNumeroSezione()                     { return numeroSezione; }
    public void setNumeroSezione(String numeroSezione)   { this.numeroSezione = numeroSezione; }

    public String getNumeroCella()                       { return numeroCella; }
    public void setNumeroCella(String numeroCella)       { this.numeroCella = numeroCella; }

    public int getCapienzaMax()                          { return capienzaMax; }
    public void setCapienzaMax(int capienzaMax)          { this.capienzaMax = capienzaMax; }

    @Override
    public String toString() {
        return "Cella{sezione='" + numeroSezione + "', cella='" + numeroCella + "', capienza=" + capienzaMax + "}";
    }
}