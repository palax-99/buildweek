package finalBW.buildweek.runner;

import finalBW.buildweek.entity.*;
import finalBW.buildweek.enumeration.TipoCliente;
import finalBW.buildweek.enumeration.TipoSede;
import finalBW.buildweek.repository.ClientiRepository;
import finalBW.buildweek.repository.ComuneRepository;
import finalBW.buildweek.service.*;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@Order(3)
public class DataRunner implements CommandLineRunner {

    private final Faker faker;
    private final Random random;
    private final ComuneService comuneService;
    private final ClientiService clientiService;
    private final IndirizzoService indirizzoService;
    private final StatoFatturaService statoFatturaService;
    private final FattureService fattureService;

    public DataRunner(Faker faker, Random random, ComuneRepository comuneRepository, ClientiRepository clientiRepository, ComuneService comuneService, ClientiService clientiService, IndirizzoService indirizzoService, StatoFatturaService statoFatturaService, FattureService fattureService) {
        this.faker = faker;
        this.random = random;
        this.comuneService = comuneService;
        this.clientiService = clientiService;
        this.indirizzoService = indirizzoService;
        this.statoFatturaService = statoFatturaService;
        this.fattureService = fattureService;
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inizio generazione dati...");

        statoFatturaService.popolaStatiFattura();
        Long clientiVoluti = 25L;
        Long numberOfClienti = clientiService.count();
        Long numberOfGenerations = 0L;

        if (numberOfClienti < clientiVoluti) {
            numberOfGenerations = clientiVoluti - numberOfClienti;
        }

        for (long i = 0; i < numberOfGenerations; i++) {
            Cliente cliente = generaCliente();
            Cliente clienteSalvato = clientiService.saveCliente(cliente);
            Indirizzo indirizzo = generaIndirizzo(clienteSalvato);
            indirizzoService.salvaIndirizzo(indirizzo);
            Long ultimoNumeroFattura = fattureService.getUltimoNumeroFattura();
            Long primoNumeroDisponibile = ultimoNumeroFattura + 1;

            List<Fattura> fatture = generaFatturePerCliente(clienteSalvato, primoNumeroDisponibile, random.nextInt(3, 20));
            fattureService.salvaFatture(fatture);
        }


        System.out.println("Dati generati");
    }

    public Cliente generaCliente() {

        String unique = UUID.randomUUID().toString().substring(0, 8);
        LocalDate dataInserimento = LocalDate.now().minusDays(random.nextInt(365));
        LocalDate dataUltimoContatto = dataInserimento.plusDays(random.nextInt((int) java.time.temporal.ChronoUnit.DAYS.between(dataInserimento, LocalDate.now()) + 1));
        String ragioneSociale = faker.company().name();
        String partitaIva = "IT" + faker.number().digits(11);

        Cliente cliente = new Cliente(
                ragioneSociale,
                partitaIva,
                "azienda." + unique + "@example.com",
                faker.number().randomDouble(2, 10_000, 2_000_000),
                "pec." + unique + "@pec.it",
                faker.phoneNumber().cellPhone(),
                "contatto." + unique + "@example.com",
                faker.name().firstName(),
                faker.name().lastName(),
                faker.phoneNumber().cellPhone(),
                TipoCliente.values()[random.nextInt(TipoCliente.values().length)]
        );

        cliente.setDataInserimento(dataInserimento);
        cliente.setDataUltimoContatto(dataUltimoContatto);
        cliente.setLogoAziendale("https://ui-avatars.com/api?name=" + java.net.URLEncoder.encode(ragioneSociale, java.nio.charset.StandardCharsets.UTF_8));

        return cliente;
    }

    public Indirizzo generaIndirizzo(Cliente cliente) {

        int randomIndex = random.nextInt((int) comuneService.count());
        Comune comuneRandom = comuneService.getComuneRandom(randomIndex);

        return new Indirizzo(
                cliente,
                faker.address().streetName(),
                faker.address().buildingNumber(),
                faker.address().secondaryAddress(),
                faker.number().digits(5),
                comuneRandom,
                TipoSede.values()[random.nextInt(TipoSede.values().length)]
        );
    }

    public List<Fattura> generaFatturePerCliente(Cliente cliente, Long primoNumero, int quantita) {

        List<Fattura> fatture = new ArrayList<>();

        for (int i = 0; i < quantita; i++) {

            LocalDate dataFattura = generaDataFattura(cliente);
            Double importo = faker.number().randomDouble(2, 100, 50_000);
            Long numero = primoNumero + i;
            StatoFattura statoFattura = statoFatturaService.getStatoFatturaRandom();

            Fattura fattura = new Fattura(
                    dataFattura,
                    importo,
                    numero,
                    cliente,
                    statoFattura
            );

            fatture.add(fattura);
        }

        return fatture;
    }

    private LocalDate generaDataFattura(Cliente cliente) {

        LocalDate dataInizio = cliente.getDataInserimento();

        if (dataInizio == null) {
            dataInizio = LocalDate.now().minusDays(365);
        }

        long giorniTraDate = ChronoUnit.DAYS.between(dataInizio, LocalDate.now());

        if (giorniTraDate <= 0) {
            return LocalDate.now();
        }

        return dataInizio.plusDays(random.nextInt((int) giorniTraDate + 1));
    }
}

