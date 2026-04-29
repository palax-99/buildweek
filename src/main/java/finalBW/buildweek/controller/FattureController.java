package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Fattura;
import finalBW.buildweek.exceptions.ValidationException;
import finalBW.buildweek.payload.FatturaDTO;
import finalBW.buildweek.payload.NewFatturaRespDTO;
import finalBW.buildweek.service.FattureService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fatture")
public class FattureController {

    private final FattureService fattureService;

    public FattureController(FattureService fattureService) {
        this.fattureService = fattureService;
    }

    // POST http://localhost:3001/fatture
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public NewFatturaRespDTO save(@RequestBody @Validated FatturaDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        Fattura nuovaFattura = this.fattureService.save(body);
        return new NewFatturaRespDTO(nuovaFattura.getFatturaId());
    }

    // GET http://localhost:3001/fatture
    @GetMapping
    public Page<Fattura> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "data") String sortBy) {
        return this.fattureService.findAll(page, size, sortBy);
    }

    // GET http://localhost:3001/fatture/cerca
    @GetMapping("/cerca")
    public Page<Fattura> search(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long statoFatturaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) Double importoMin,
            @RequestParam(required = false) Double importoMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {

        return this.fattureService.findFiltered(clienteId, statoFatturaId, data, anno,
                importoMin, importoMax, page, size, sortBy);
    }

    // GET http://localhost:3001/fatture/{id}
    @GetMapping("/{fatturaId}")
    public Fattura getById(@PathVariable Long fatturaId) {
        return this.fattureService.findById(fatturaId);
    }

    // PUT http://localhost:3001/fatture/{id}
    @PutMapping("/{fatturaId}")
    public Fattura updateById(@PathVariable Long fatturaId,
                              @RequestBody @Validated FatturaDTO body,
                              BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        return this.fattureService.findByIdAndUpdate(fatturaId, body);
    }

    // PATCH http://localhost:3001/fatture/{id}/stato
    // Endpoint dedicato per cambiare solo lo stato della fattura
    @PatchMapping("/{fatturaId}/stato")
    public Fattura updateStato(@PathVariable Long fatturaId,
                               @RequestParam Long nuovoStatoId) {
        return this.fattureService.findByIdAndUpdateStato(fatturaId, nuovoStatoId);
    }

    // DELETE http://localhost:3001/fatture/{id}
    @DeleteMapping("/{fatturaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteById(@PathVariable Long fatturaId) {
        this.fattureService.findByIdAndDelete(fatturaId);
    }
}
