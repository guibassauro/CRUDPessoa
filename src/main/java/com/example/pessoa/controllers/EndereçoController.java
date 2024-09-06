package com.example.pessoa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pessoa.entities.requests.CreateEnderecoRequest;
import com.example.pessoa.entities.requests.UpdateEnderecoRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import com.example.pessoa.services.EnderecoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EndereçoController {

    final EndereçoRepository endereçoRepository;
    final PessoaRepository pessoaRepository;

    final EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<Object> achaTodos(){
        return enderecoService.achaTodos();
    }

    @PostMapping
    public ResponseEntity<Object> createEndereco(
        @RequestBody CreateEnderecoRequest createEndereco
    ) {
        return enderecoService.criaEndereco(createEndereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEndereço(
        @PathVariable Long id, @RequestBody UpdateEnderecoRequest updateEndereco
    ) {
        return enderecoService.atualizaEndereco(id, updateEndereco);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEndereco(
        @PathVariable Long id
    ) {
        return enderecoService.deletaEndereco(id);
    }
}    
