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

import com.example.pessoa.entities.requests.CreatePessoaRequest;
import com.example.pessoa.entities.requests.UpdateFavoritoRequest;
import com.example.pessoa.entities.requests.UpdatePessoaRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import com.example.pessoa.services.PessoaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {
    
    final PessoaRepository pessoaRepository;
    final EndereçoRepository endereçoRepository;
    final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Object> listaTodosAsPessoa(){
        return pessoaService.achaTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getIdadeDaPessoa(
        @PathVariable Long id
    ) {
        return pessoaService.pegaIdadeDaPessoaPorId(id);
    }

    @PostMapping
    public ResponseEntity<Object> criaPessoa(
        @RequestBody CreatePessoaRequest createPessoa
    ) {
        return pessoaService.criaPessoa(createPessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePessoa(
        @PathVariable Long id, @RequestBody UpdatePessoaRequest updatePessoa
    ) {
        return pessoaService.atualizaPessoa(id, updatePessoa);
    }

    @PutMapping("favorito/{id}")
    public ResponseEntity<Object> adicionaFavorito(
        @PathVariable Long id, @RequestBody UpdateFavoritoRequest updateFavorito
    ){
        return pessoaService.adicionaEnderecoFavoritoParaPessoa(id, updateFavorito);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePessoa(
        @PathVariable Long id
    ) {
        return pessoaService.deletaPessoa(id);
    }
}
