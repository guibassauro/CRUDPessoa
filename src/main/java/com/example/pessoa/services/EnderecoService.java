package com.example.pessoa.services;

import org.springframework.http.ResponseEntity;

import com.example.pessoa.entities.requests.CreateEnderecoRequest;
import com.example.pessoa.entities.requests.UpdateEnderecoRequest;


public interface EnderecoService {
    
    ResponseEntity<Object> achaTodos();

    ResponseEntity<Object> criaEndereco(CreateEnderecoRequest criaEndereco);
    
    ResponseEntity<Object> atualizaEndereco(Long id, UpdateEnderecoRequest atualizaEndereco);

    ResponseEntity<Object> deletaEndereco(Long id);
}
