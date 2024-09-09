package com.example.pessoa.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.pessoa.entities.requests.CriarPessoaRequest;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.AtualizarFavoritoRequest;
import com.example.pessoa.entities.requests.AtualizarPessoaRequest;

public interface PessoaService {

    Page<Pessoa> achaTodos(Pageable pageable);

    ResponseEntity<Object> pegaIdadeDaPessoaPorId(Long id);

    ResponseEntity<Object> criaPessoa(CriarPessoaRequest criaPessoa);

    ResponseEntity<Object> atualizaPessoa(Long id, AtualizarPessoaRequest atualizaPessoa);

    ResponseEntity<Object> adicionaEnderecoFavoritoParaPessoa(Long id, AtualizarFavoritoRequest atualizaFavorito);

    ResponseEntity<Object> deletaPessoa(Long id);
}
