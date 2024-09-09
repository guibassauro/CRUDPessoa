package com.example.pessoa.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pessoa.entities.requests.CriarPessoaRequest;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.AtualizarFavoritoRequest;
import com.example.pessoa.entities.requests.AtualizarPessoaRequest;
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
    public ResponseEntity<Page<Pessoa>> achaTodasAsPessoas(Pageable pageable){
        return ResponseEntity.ok(pessoaRepository.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getIdadeDaPessoaPorId(
        @PathVariable Long id
    ) {
        return pessoaService.pegaIdadeDaPessoaPorId(id);
    }

    @PostMapping
    public ResponseEntity<Object> criaPessoa(
        @RequestBody CriarPessoaRequest createPessoa
    ) {
        return pessoaService.criaPessoa(createPessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizaPessoa(
        @PathVariable Long id, @RequestBody AtualizarPessoaRequest updatePessoa
    ) {
        return pessoaService.atualizaPessoa(id, updatePessoa);
    }

    @PutMapping("favorito/{id}")
    public ResponseEntity<Object> adicionaEnderecoFavoritoPorId(
        @PathVariable Long id, @RequestBody AtualizarFavoritoRequest updateFavorito
    ){
        return pessoaService.adicionaEnderecoFavoritoParaPessoa(id, updateFavorito);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletaPessoaPorId(
        @PathVariable Long id
    ) {
        return pessoaService.deletaPessoa(id);
    }
}
