package com.testequalificacao.cadastrodelocalizacao.controller;

import com.testequalificacao.cadastrodelocalizacao.repository.EnderecoRepository;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.testequalificacao.cadastroDeLocalizacao.model.Endereco;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Classe responsável por expor cada URI disponível.
 *
 * @author Mateus
 */
@RestController
@RequestMapping({"/endereco"})
public class EnderecoController {

    private final EnderecoRepository repository;

    public EnderecoController(EnderecoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Endereco create(@RequestBody Endereco endereco) {
        return repository.save(endereco);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@PathVariable("id") long id,
            @RequestBody Endereco endereco) {

        return repository.findById(id).map(e -> {
            e.setStreetName(endereco.getStreetName());
            e.setNumber(endereco.getNumber());
            e.setComplement(endereco.getComplement());
            e.setNeighbourhood(endereco.getNeighbourhood());
            e.setCity(endereco.getCity());
            e.setState(endereco.getState());
            e.setCountry(endereco.getCountry());
            e.setZipcode(endereco.getZipcode());
            e.setLatitude(endereco.getLatitude());
            e.setLongitude(endereco.getLongitude());

            Endereco updated = repository.save(e);

            return ResponseEntity.ok().body(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity findById(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Endereco> findAll() {
        return repository.findAll();
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable long id) {
        return repository.findById(id).map(record -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
