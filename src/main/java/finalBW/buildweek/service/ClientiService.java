package finalBW.buildweek.service;

import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.exceptions.BadRequestException;
import finalBW.buildweek.exceptions.NotFoundException;
import finalBW.buildweek.payload.ClienteDTO;
import finalBW.buildweek.repository.ClientiRepository;
import finalBW.buildweek.repository.FattureRepository;
import finalBW.buildweek.specifications.ClienteSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
public class ClientiService {

    private final ClientiRepository clientiRepository;
    private final FattureRepository fattureRepository;
    // Mi serve anche FattureRepository per cancellare le fatture del cliente
    // prima di eliminarlo (cascade gestito a mano per via della relazione unidirezionale)

    public ClientiService(ClientiRepository clientiRepository, FattureRepository fattureRepository) {
        this.clientiRepository = clientiRepository;
        this.fattureRepository = fattureRepository;
    }

    // CREA un nuovo cliente
    public Cliente save(ClienteDTO body) {
        // 1. Controllo che i campi unique non siano gia in uso nel db
        // Se non li controllo qua, il db mi tira un errore brutto al momento dell INSERT
        if (this.clientiRepository.existsByPartitaIva(body.partitaIva()))
            throw new BadRequestException("La partita IVA " + body.partitaIva() + " e gia in uso!");
        if (this.clientiRepository.existsByEmail(body.email()))
            throw new BadRequestException("L email " + body.email() + " e gia in uso!");
        if (this.clientiRepository.existsByPec(body.pec()))
            throw new BadRequestException("La PEC " + body.pec() + " e gia in uso!");
        if (this.clientiRepository.existsByEmailContatto(body.emailContatto()))
            throw new BadRequestException("L email del contatto " + body.emailContatto() + " e gia in uso!");

        // 2. Creo l entita Cliente partendo dal DTO
        Cliente nuovoCliente = new Cliente(
                body.ragioneSociale(),
                body.partitaIva(),
                body.email(),
                body.fatturatoAnnuale(),
                body.pec(),
                body.telefono(),
                body.emailContatto(),
                body.nomeContatto(),
                body.cognomeContatto(),
                body.telefonoContatto(),
                body.tipoCliente()
        );

        // 3. Setto la dataInserimento automaticamente (oggi)
        // Non la prendo dal DTO perche non e una cosa che il client deve decidere,
        // e una info di sistema
        nuovoCliente.setDataInserimento(LocalDate.now());

        // 4. Logo aziendale di default (potra essere aggiornato dopo via Cloudinary)
        nuovoCliente.setLogoAziendale("https://ui-avatars.com/api?name=" + body.ragioneSociale());

        // 5. Salvo nel db
        Cliente clienteSalvato = this.clientiRepository.save(nuovoCliente);

        // 6. Log
        log.info("Cliente con id " + clienteSalvato.getClientiId() + " salvato correttamente");

        return clienteSalvato;
    }

    // LISTA tutti i clienti senza filtri (paginazione + ordinamento)
    // Soddisfa la task "Ordinare i clienti" delle slide
    public Page<Cliente> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.clientiRepository.findAll(pageable);
    }

    // LISTA con filtri dinamici (uso le Specification!)
    // Soddisfa la task "Filtrare i clienti" delle slide
    // Tutti i parametri sono opzionali: chi e null non viene aggiunto alla query
    public Page<Cliente> findFiltered(
            String parteDelNome,
            Double fatturatoMin,
            Double fatturatoMax,
            LocalDate dataInserimento,
            LocalDate dataUltimoContatto,
            int page, int size, String sortBy) {

        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;

        // Parto da una Specification "vuota" (nessun filtro = WHERE 1=1)
        // e poi aggiungo solo i filtri che il client ha effettivamente passato
        Specification<Cliente> spec = Specification.unrestricted();

        // Per ogni parametro non null/non vuoto, aggiungo la spec corrispondente con .and()
        if (parteDelNome != null && !parteDelNome.isBlank()) {
            spec = spec.and(ClienteSpecifications.ragioneSocialeContiene(parteDelNome));
        }
        if (fatturatoMin != null) {
            spec = spec.and(ClienteSpecifications.fatturatoMaggioreUguale(fatturatoMin));
        }
        if (fatturatoMax != null) {
            spec = spec.and(ClienteSpecifications.fatturatoMinoreUguale(fatturatoMax));
        }
        if (dataInserimento != null) {
            spec = spec.and(ClienteSpecifications.dataInserimentoUguale(dataInserimento));
        }
        if (dataUltimoContatto != null) {
            spec = spec.and(ClienteSpecifications.dataUltimoContattoUguale(dataUltimoContatto));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        // findAll(Spec, Pageable) viene da JpaSpecificationExecutor
        return this.clientiRepository.findAll(spec, pageable);
    }

    // TROVA singolo cliente per id
    public Cliente findById(Long clienteId) {
        return this.clientiRepository.findById(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + clienteId + " non trovato!"));
    }

    // MODIFICA cliente esistente
    public Cliente findByIdAndUpdate(Long clienteId, ClienteDTO body) {
        // 1. Cerco il cliente (se non c e, findById tira NotFoundException)
        Cliente trovato = this.findById(clienteId);

        // 2. Controllo unicita SOLO sui campi che stanno effettivamente cambiando
        // Altrimenti se mando lo stesso valore, l existsBy mi torna true e tira erroneamente eccezione
        if (!trovato.getPartitaIva().equals(body.partitaIva())
                && this.clientiRepository.existsByPartitaIva(body.partitaIva()))
            throw new BadRequestException("La partita IVA " + body.partitaIva() + " e gia in uso!");

        if (!trovato.getEmail().equals(body.email())
                && this.clientiRepository.existsByEmail(body.email()))
            throw new BadRequestException("L email " + body.email() + " e gia in uso!");

        if (!trovato.getPec().equals(body.pec())
                && this.clientiRepository.existsByPec(body.pec()))
            throw new BadRequestException("La PEC " + body.pec() + " e gia in uso!");

        if (!trovato.getEmailContatto().equals(body.emailContatto())
                && this.clientiRepository.existsByEmailContatto(body.emailContatto()))
            throw new BadRequestException("L email del contatto " + body.emailContatto() + " e gia in uso!");

        // 3. Aggiorno i campi modificabili
        // Nota: NON tocco dataInserimento (e una info di sistema, una volta settata non cambia)
        // e NON tocco clientiId (e la PK)
        trovato.setRagioneSociale(body.ragioneSociale());
        trovato.setPartitaIva(body.partitaIva());
        trovato.setEmail(body.email());
        trovato.setFatturatoAnnuale(body.fatturatoAnnuale());
        trovato.setPec(body.pec());
        trovato.setTelefono(body.telefono());
        trovato.setEmailContatto(body.emailContatto());
        trovato.setNomeContatto(body.nomeContatto());
        trovato.setCognomeContatto(body.cognomeContatto());
        trovato.setTelefonoContatto(body.telefonoContatto());
        trovato.setTipoCliente(body.tipoCliente());

        // 4. Aggiorno la dataUltimoContatto a OGGI
        // Logica di business: se sto modificando un cliente vuol dire che l ho ricontattato
        trovato.setDataUltimoContatto(LocalDate.now());

        // 5. Salvo (qui save fara UPDATE perche l id c e gia)
        Cliente aggiornato = this.clientiRepository.save(trovato);

        log.info("Cliente con id " + aggiornato.getClientiId() + " modificato correttamente");
        return aggiornato;
    }

    // CANCELLA cliente per id
    @Transactional
    // Devo annotarlo @Transactional altrimenti deleteByCliente non funziona
    // (Spring Data lo richiede per i delete custom: deve girare dentro una transazione)
    public void findByIdAndDelete(Long clienteId) {
        // 1. Cerco il cliente
        Cliente trovato = this.findById(clienteId);

        // 2. PRIMA cancello tutte le fatture associate al cliente
        // Questo e il "cascade gestito a mano": siccome la relazione e unidirezionale
        // (Cliente non ha la lista di Fatture), non posso usare @OneToMany cascade
        // Quindi devo farlo qui esplicitamente
        this.fattureRepository.deleteByCliente(trovato);

        // 3. POI cancello il cliente
        // Se invertissi l ordine, le fatture rimarrebbero "orfane" con FK a un cliente inesistente
        this.clientiRepository.delete(trovato);

        log.info("Cliente con id " + clienteId + " e tutte le sue fatture cancellati");
    }
}
