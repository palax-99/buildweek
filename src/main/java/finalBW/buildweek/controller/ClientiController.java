package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Cliente;
import finalBW.buildweek.exceptions.ValidationException;
import finalBW.buildweek.payload.ClienteDTO;
import finalBW.buildweek.payload.NewClienteRespDTO;
import finalBW.buildweek.service.ClientiService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClientiController {

    private final ClientiService clientiService;

    public ClientiController(ClientiService clientiService) {
        this.clientiService = clientiService;
    }

    // POST http://localhost:3001/clienti
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public NewClienteRespDTO save(@RequestBody @Validated ClienteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        Cliente nuovoCliente = this.clientiService.save(body);
        return new NewClienteRespDTO(nuovoCliente.getClientiId());
    }

    // GET http://localhost:3001/clienti
    @GetMapping
    public Page<Cliente> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "ragioneSociale") String sortBy,
                                @RequestParam(defaultValue = "asc") String direction) {
        return this.clientiService.findAll(page, size, sortBy, direction);
    }

    // GET http://localhost:3001/clienti/cerca
    @GetMapping("/cerca")
    public Page<Cliente> search(
            @RequestParam(required = false) String parteDelNome,
            @RequestParam(required = false) Double fatturatoMin,
            @RequestParam(required = false) Double fatturatoMax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInserimento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataUltimoContatto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy) {

        return this.clientiService.findFiltered(parteDelNome, fatturatoMin, fatturatoMax,
                dataInserimento, dataUltimoContatto, page, size, sortBy);
    }

    // GET http://localhost:3001/clienti/{id}
    @GetMapping("/{clienteId}")
    public Cliente getById(@PathVariable Long clienteId) {
        return this.clientiService.findById(clienteId);
    }

    // PUT http://localhost:3001/clienti/{id}
    @PutMapping("/{clienteId}")
    public Cliente updateById(@PathVariable Long clienteId,
                              @RequestBody @Validated ClienteDTO body,
                              BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        return this.clientiService.findByIdAndUpdate(clienteId, body);
    }

    // DELETE http://localhost:3001/clienti/{id}
    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteById(@PathVariable Long clienteId) {
        this.clientiService.findByIdAndDelete(clienteId);
    }
}
