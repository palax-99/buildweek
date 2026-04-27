package finalBW.buildweek.data.utility;

import finalBW.buildweek.enumeration.Regione;

public record ProvinceCsvRecord(
        String sigla,
        String provinciaNome,
        Regione regione
) {
    @Override
    public String toString() {
        return "ProvinceCsvRecord{" +
                "sigla='" + sigla + '\'' +
                ", provinciaNome='" + provinciaNome + '\'' +
                ", regione=" + regione +
                '}';
    }
}
