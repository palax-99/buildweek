package finalBW.buildweek.data.utility;

public record ComuniCsvRecord(
        String comuneNome,
        String provinciaNome
) {

    @Override
    public String toString() {
        return "ComuniCsvRecord{" +
                "comuneNome='" + comuneNome + '\'' +
                ", provinciaNome='" + provinciaNome + '\'' +
                '}';
    }
}
