package com.testequalificacao.cadastrodelocalizacao.controller;

import com.testequalificacao.cadastroDeLocalizacao.controller.integracaoexternageocoding.IntegracaoExternaGeocodingWS;
import com.testequalificacao.cadastroDeLocalizacao.controller.integracaoexternageocoding.ws.AddressResponse;
import com.testequalificacao.cadastroDeLocalizacao.controller.integracaoexternageocoding.ws.Result;
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

    /**
     * Responsável pela criação de um novo Endereço.
     *
     * @param endereco O Endereço a ser criado.
     *
     * @return O Endereço criado.
     */
    @PostMapping
    public Endereco create(@RequestBody Endereco endereco) {
        validarGeolocalizacao(endereco);

        return repository.save(endereco);
    }

    /**
     * Atualiza o Endereço com o id especificado.
     *
     * @param id O id do Endereço a ser atualizado.
     * @param endereco O Endereço com os dados atualizados.
     *
     * @return O Endereço atualizado.
     */
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

            validarGeolocalizacao(e);

            Endereco updated = repository.save(e);

            return ResponseEntity.ok().body(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna o Endereço do id especificado.
     *
     * @param id O ID do Endereço.
     *
     * @return O Endereço com o ID buscado.
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity findById(@PathVariable long id) {
        return repository.findById(id)
                .map(e -> ResponseEntity.ok().body(e))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna todos o Endereços cadastrados.
     *
     * @return Uma lista de Endereços.
     */
    @GetMapping
    public List<Endereco> findAll() {
        return repository.findAll();
    }

    /**
     * Remove o Endereço cadastrado através do id.
     *
     * @param id ID do Endereço a ser removido.
     *
     * @return Resposta positiva ou negativa a remoção do Endereço.
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable long id) {
        return repository.findById(id).map(e -> {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Valida a Latitude e a Longitude através do Endereço.
     *
     * @param endereco O Endereço a ser validado.
     */
    public void validarGeolocalizacao(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalStateException("O endereço não pode ser nulo");
        }

        if (endereco.getLatitude() == null
                || endereco.getLongitude() == null) {
            IntegracaoExternaGeocodingWS ws
                    = new IntegracaoExternaGeocodingWS();

            StringBuffer sb = new StringBuffer();
            String virgula = ",";
            String espaco = " ";
            sb.append(endereco.getNumber());
            sb.append(espaco);
            sb.append(endereco.getStreetName());
            sb.append(virgula);
            sb.append(espaco);
            sb.append(endereco.getNeighbourhood());
            sb.append(virgula);
            sb.append(espaco);
            sb.append(endereco.getState());

            AddressResponse dadosGeolocalicacao
                    = ws.getDadosGeolocalicacao(sb.toString());

            if (dadosGeolocalicacao.getResults().size() <= 0
                    || dadosGeolocalicacao.getResults() == null) {
                System.out.println("O endereço não foi localizado");

                return;
            }

            Result result = dadosGeolocalicacao.getResults().stream()
                    .findFirst().get();

            endereco.setLatitude(result.getGeometry().getLocation().getLat());
            endereco.setLongitude(result.getGeometry().getLocation().getLng());
        }
    }
}
