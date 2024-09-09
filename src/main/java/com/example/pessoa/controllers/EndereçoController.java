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

import com.example.pessoa.entities.requests.CriarEnderecoRequest;
import com.example.pessoa.entities.requests.AtualizarEnderecoRequest;
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
    public ResponseEntity<Object> achaTodosOsEnderecos(){
        return enderecoService.achaTodos();
    }

    @PostMapping
    public ResponseEntity<Object> criaNovoEndereco(
        @RequestBody CriarEnderecoRequest criaEndereco
    ) {
        return enderecoService.criaEndereco(criaEndereco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizaEnderecoPorId(
        @PathVariable Long id, @RequestBody AtualizarEnderecoRequest atualizaEndereco
    ) {
        return enderecoService.atualizaEndereco(id, atualizaEndereco);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletaEnderecoPorId(
        @PathVariable Long id
    ) {
        return enderecoService.deletaEndereco(id);
    }
}    
