package finalBW.buildweek.controller;

import finalBW.buildweek.entity.StatoFattura;
import finalBW.buildweek.exceptions.ValidationException;
import finalBW.buildweek.payload.NewStatoFatturaRespDTO;
import finalBW.buildweek.payload.StatoFatturaDTO;
import finalBW.buildweek.service.StatiFatturaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stati-fattura")
public class StatiFatturaController {

    private final StatiFatturaService statiFatturaService;

    public StatiFatturaController(StatiFatturaService statiFatturaService) {
        this.statiFatturaService = statiFatturaService;
    }

    // POST http://localhost:3001/stati-fattura
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public NewStatoFatturaRespDTO save(@RequestBody @Validated StatoFatturaDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        StatoFattura nuovoStato = this.statiFatturaService.save(body);
        return new NewStatoFatturaRespDTO(nuovoStato.getStatoFatturaId());
    }

    // GET http://localhost:3001/stati-fattura
    @GetMapping
    public Page<StatoFattura> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "denominazione") String sortBy) {
        return this.statiFatturaService.findAll(page, size, sortBy);
    }

    // GET http://localhost:3001/stati-fattura/{id}
    @GetMapping("/{statoId}")
    public StatoFattura getById(@PathVariable Long statoId) {
        return this.statiFatturaService.findById(statoId);
    }

    // PUT http://localhost:3001/stati-fattura/{id}
    @PutMapping("/{statoId}")
    public StatoFattura updateById(@PathVariable Long statoId,
                                   @RequestBody @Validated StatoFatturaDTO body,
                                   BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        return this.statiFatturaService.findByIdAndUpdate(statoId, body);
    }

    // DELETE http://localhost:3001/stati-fattura/{id}
    @DeleteMapping("/{statoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteById(@PathVariable Long statoId) {
        this.statiFatturaService.findByIdAndDelete(statoId);
    }
}
