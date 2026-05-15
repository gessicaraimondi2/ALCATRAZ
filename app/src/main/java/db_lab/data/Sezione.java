package db_lab.data;

public class Sezione {

    public enum NomeSezione {
        Ordinaria,
        Alta_Sicurezza,
        Isolamento,
        Unita_Assistenza_Clinica;

        /** Restituisce il valore testuale corrispondente al DB */
        public String toDBString() {
            return switch (this) {
                case Ordinaria               -> "Ordinaria";
                case Alta_Sicurezza          -> "Alta Sicurezza";
                case Isolamento              -> "Isolamento";
                case Unita_Assistenza_Clinica -> "Unità Assistenza Clinica";
            };
        }
    }

    private String numeroSezione;
    private NomeSezione nomeSezione;

    public Sezione() {}

    public Sezione(String numeroSezione, NomeSezione nomeSezione) {
        this.numeroSezione = numeroSezione;
        this.nomeSezione   = nomeSezione;
    }

    public String getNumeroSezione()                    { return numeroSezione; }
    public void setNumeroSezione(String numeroSezione)  { this.numeroSezione = numeroSezione; }

    public NomeSezione getNomeSezione()                 { return nomeSezione; }
    public void setNomeSezione(NomeSezione nomeSezione) { this.nomeSezione = nomeSezione; }

    @Override
    public String toString() {
        return "Sezione{numero='" + numeroSezione + "', nome=" + nomeSezione + "}";
    }
}