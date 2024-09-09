package com.example.pessoa.services;

import org.springframework.http.ResponseEntity;

import com.example.pessoa.entities.requests.CriarEnderecoRequest;
import com.example.pessoa.entities.requests.AtualizarEnderecoRequest;


public interface EnderecoService {
    
    ResponseEntity<Object> achaTodos();

    ResponseEntity<Object> criaEndereco(CriarEnderecoRequest criaEndereco);
    
    ResponseEntity<Object> atualizaEndereco(Long id, AtualizarEnderecoRequest atualizaEndereco);

    ResponseEntity<Object> deletaEndereco(Long id);
}
