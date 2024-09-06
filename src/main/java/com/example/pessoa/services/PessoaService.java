package com.example.pessoa.services;

import org.springframework.http.ResponseEntity;

import com.example.pessoa.entities.requests.CreatePessoaRequest;
import com.example.pessoa.entities.requests.UpdateFavoritoRequest;
import com.example.pessoa.entities.requests.UpdatePessoaRequest;

public interface PessoaService {

    ResponseEntity<Object> achaTodos();

    ResponseEntity<Object> pegaIdadeDaPessoaPorId(Long id);

    ResponseEntity<Object> criaPessoa(CreatePessoaRequest criaPessoa);

    ResponseEntity<Object> atualizaPessoa(Long id, UpdatePessoaRequest atualizaPessoa);

    ResponseEntity<Object> adicionaEnderecoFavoritoParaPessoa(Long id, UpdateFavoritoRequest atualizaFavorito);

    ResponseEntity<Object> deletaPessoa(Long id);
}
