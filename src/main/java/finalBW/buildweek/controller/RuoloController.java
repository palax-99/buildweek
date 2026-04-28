package finalBW.buildweek.controller;

import finalBW.buildweek.entity.Ruolo;
import finalBW.buildweek.payload.NuovoRuoloDTO;
import finalBW.buildweek.service.RuoloService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ruoli")
public class RuoloController {

    private final RuoloService rService;

    public RuoloController(RuoloService rService) {
        this.rService = rService;
    }

    @GetMapping
    public Page<Ruolo> findAll(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "denominazione") String sortBy) {
        return rService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ruolo save(@RequestBody @Valid NuovoRuoloDTO body) {
        return rService.save(body);
    }

    @GetMapping("/{ruoloId}")
    public Ruolo getById(@PathVariable long ruoloId) {
        return rService.findById(ruoloId);
    }

    @DeleteMapping("/{ruoloId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long ruoloId) {
        rService.delete(ruoloId);
    }
}