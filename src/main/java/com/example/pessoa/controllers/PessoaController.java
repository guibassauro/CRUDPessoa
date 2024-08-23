package com.example.pessoa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pessoa.entities.Endereço;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.CreatePessoaRequest;
import com.example.pessoa.entities.requests.UpdateFavoritoRequest;
import com.example.pessoa.entities.requests.UpdatePessoaRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {
    
    final PessoaRepository pessoaRepository;
    final EndereçoRepository endereçoRepository;

    @GetMapping
    public ResponseEntity<Object> getPessoas(){
        return ResponseEntity.ok().body(pessoaRepository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getIdadeDaPessoa(
        @PathVariable Long id
    ) {
        Optional<Pessoa> pessoaExiste = pessoaRepository.findById(id);

        if(pessoaExiste.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        int idade = pessoaExiste.get().calculaIdade();

        return ResponseEntity.ok().body(pessoaExiste.get().getNome() + " tem " + idade + " anos");
        
    }

    @PostMapping
    public ResponseEntity<Object> createPessoa(
        @RequestBody CreatePessoaRequest createPessoa
    ) {
        List<Endereço> enderecosRequest = endereçoRepository.findAllById(createPessoa.getEnderecos_id());

        if(enderecosRequest.isEmpty()){
            return ResponseEntity.badRequest().body("Uma pessoa precisa de ao menos um endereço para ser cadastrada");
        }

        Pessoa novaPessoa = new Pessoa(
            null,
            createPessoa.getNome(),
            createPessoa.getDataDeNascimento(),
            createPessoa.getCpf(),
            enderecosRequest,
            null
        );
        
        enderecosRequest.forEach(endereco -> {
            if(endereco.getPessoa() == null){
                endereco.addPessoa(novaPessoa);   
            }
        });

        return ResponseEntity.ok(pessoaRepository.save(novaPessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePessoa(
        @PathVariable Long id, @RequestBody UpdatePessoaRequest updatePessoa
    ) {

        Optional<Pessoa> existePessoa = pessoaRepository.findById(id);

        if(existePessoa.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        existePessoa.get().getEnderecos().forEach(endereco -> {
            endereco.setPessoa(null);
        });

        List<Endereço> updateEnderecos = endereçoRepository.findAllById(updatePessoa.getEnderecos_id());

        if(updateEnderecos.isEmpty()){
            return ResponseEntity.badRequest().body("Uma pessoa precisa de pelo menos um endereço");
        }

        Pessoa atualizaPessoa = new Pessoa();

        atualizaPessoa.setId(id);
        atualizaPessoa.setNome(updatePessoa.getNome());
        atualizaPessoa.setDataDeNascimento(updatePessoa.getDataDeNascimento());
        atualizaPessoa.setCpf(updatePessoa.getCpf());
        atualizaPessoa.setEnderecos(updateEnderecos);
        
        updateEnderecos.forEach(endereco -> {
            endereco.addPessoa(atualizaPessoa);
        });

        pessoaRepository.save(atualizaPessoa);

        return ResponseEntity.ok().body(atualizaPessoa);
    }

    @PutMapping("favorito/{id}")
    public ResponseEntity<Object> adicionaFavorito(
        @PathVariable Long id, @RequestBody UpdateFavoritoRequest updateFavorito
    ){
        Optional<Pessoa> existePessoa = pessoaRepository.findById(id);
        Optional<Endereço> existeEndereco = endereçoRepository.findById(updateFavorito.getEnderecoFavorito_id());

        if(existeEndereco.isEmpty() || existePessoa.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(!existePessoa.get().getEnderecos().contains(existeEndereco.get())){
            return ResponseEntity.badRequest().body("Esta pessoa não possui este endereço");
        }

        Pessoa atualizaPessoa = existePessoa.get();

        atualizaPessoa.setEnderecoFavorito(existeEndereco.get());

        return ResponseEntity.ok().body(pessoaRepository.save(atualizaPessoa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePessoa(
        @PathVariable Long id
    ) {
        Optional<Pessoa> existePessoa = pessoaRepository.findById(id);

        if(existePessoa.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        existePessoa.get().setEnderecoFavorito(null);

        List<Endereço> enderecosDelete = existePessoa.get().getEnderecos();

        endereçoRepository.deleteAll(enderecosDelete);
        pessoaRepository.deleteById(id);
        
        return ResponseEntity.ok().body("Pessoa deletada");
    }
}
