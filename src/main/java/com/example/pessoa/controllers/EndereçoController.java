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
import com.example.pessoa.entities.requests.CreateEnderecoRequest;
import com.example.pessoa.entities.requests.UpdateEnderecoRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EndereçoController {

    final EndereçoRepository endereçoRepository;
    final PessoaRepository pessoaRepository;

    @GetMapping
    public List<Endereço> getEnderecos(){
        return endereçoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> createEndereco(
        @RequestBody CreateEnderecoRequest createEndereco
    ) {

        Endereço novoEndereco = new Endereço(
            null,
            createEndereco.getRua(),
            createEndereco.getNumero(),
            createEndereco.getCidade(),
            createEndereco.getEstado(),
            createEndereco.getCep(),
            null
        );

        return ResponseEntity.ok(endereçoRepository.save(novoEndereco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEndereço(
        @PathVariable Long id, @RequestBody UpdateEnderecoRequest updateEndereco
    ) {
        Optional<Endereço> existeEndereco = endereçoRepository.findById(id);

        if(existeEndereco.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Endereço atualizaEndereço = new Endereço();

        atualizaEndereço.setId(id);
        atualizaEndereço.setRua(updateEndereco.getRua());
        atualizaEndereço.setNumero(updateEndereco.getNumero());
        atualizaEndereço.setCidade(updateEndereco.getCidade());
        atualizaEndereço.setEstado(updateEndereco.getEstado());
        atualizaEndereço.setCep(updateEndereco.getCep());
        atualizaEndereço.setPessoa(existeEndereco.get().getPessoa());

        endereçoRepository.save(atualizaEndereço);

        return ResponseEntity.ok().body(atualizaEndereço);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEndereco(
        @PathVariable Long id
    ) {
        Optional<Endereço> existeEndereco = endereçoRepository.findById(id);

        if(existeEndereco.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(existeEndereco.get().getPessoa().getEnderecos().size() < 2){
            return ResponseEntity.badRequest().body("Se você deletar esse endereço " + 
            existeEndereco.get().getPessoa().getNome() + " vai ficar sem endereços");
        }

        if(existeEndereco.get().getPessoa().getEnderecoFavorito().getId() == id){
            return ResponseEntity.badRequest().body("Não é possível deletar um endereço favorito");
        }

        endereçoRepository.deleteById(existeEndereco.get().getId());

        return ResponseEntity.ok().body("Endereço deletado");
    }
}    
