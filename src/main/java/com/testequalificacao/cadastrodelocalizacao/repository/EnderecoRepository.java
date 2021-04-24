package com.testequalificacao.cadastrodelocalizacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.testequalificacao.cadastroDeLocalizacao.model.Endereco;

/**
 * Classe responsável por prover os métodos das operações.
 * 
 * @author Mateus
 */
public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
}
