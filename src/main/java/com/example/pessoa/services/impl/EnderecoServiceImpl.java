package com.example.pessoa.services.impl;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.pessoa.entities.Endereço;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.CriarEnderecoRequest;
import com.example.pessoa.entities.requests.AtualizarEnderecoRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import com.example.pessoa.services.EnderecoService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {

    final PessoaRepository pessoaRepository;
    final EndereçoRepository endereçoRepository;

    @Override
    public ResponseEntity<Object> achaTodos(){
        return ResponseEntity.ok().body(endereçoRepository.findAll());
    }

    @Override
    public ResponseEntity<Object> criaEndereco(CriarEnderecoRequest criaEndereco){
        
        Endereço novoEndereco = new Endereço(
            null,
            criaEndereco.getRua(),
            criaEndereco.getNumero(),
            criaEndereco.getCidade(),
            criaEndereco.getEstado(),
            criaEndereco.getCep()
        );

        endereçoRepository.save(novoEndereco);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(novoEndereco);
    }

    @Override
    public ResponseEntity<Object> atualizaEndereco(Long id, AtualizarEnderecoRequest atualizaEndereco){
        if(!confereEndereco(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("O endereço " + id + " não foi encontrado");
        }

        Endereço enderecoAtualizado = endereçoRepository.findById(id).get();

        enderecoAtualizado.setRua(atualizaEndereco.getRua());
        enderecoAtualizado.setNumero(atualizaEndereco.getNumero());
        enderecoAtualizado.setCidade(atualizaEndereco.getCidade());
        enderecoAtualizado.setEstado(atualizaEndereco.getEstado());
        enderecoAtualizado.setCep(atualizaEndereco.getCep());

        endereçoRepository.save(enderecoAtualizado);

        return ResponseEntity.ok().body(enderecoAtualizado);

    }

    public ResponseEntity<Object> deletaEndereco(Long id){
        if(!confereEndereco(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("O endereço " + id + " não foi encontrado");
        }

        List<Pessoa> proprietarios = confereDonosDesteEndereco(id);
        
        for(Pessoa proprietario : proprietarios){
            if(proprietario.getEnderecos().size() < 2){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Se você deletar este endereço " + 
                                                        proprietario.getNome() + " vai ficar sem endereços");
            }
        }

        for(Pessoa proprietario : proprietarios){
            if(proprietario.getEnderecoFavorito().getId() == id){
                proprietario.setEnderecoFavorito(null);
            }
        }

        endereçoRepository.deleteById(id);

        return ResponseEntity.ok().body("Endereço deletado");
    }
    
    

    public boolean conferePessoa(Long id){
        Optional<Pessoa> existePessoa = pessoaRepository.findById(id);

        if(existePessoa.isEmpty()){
            return false;
        }

        return true;
    }

    public boolean confereEndereco(Long id){
        Optional<Endereço> existeEndereco = endereçoRepository.findById(id);

        if(existeEndereco.isEmpty()){
            return false;
        }

        return true;
    }

    public List<Pessoa> confereDonosDesteEndereco(Long id){
        List<Pessoa> proprietarios = new ArrayList<>();

        for(Pessoa pessoa : pessoaRepository.findAll()){
            if(pessoa.getEnderecos().contains(endereçoRepository.findById(id).get())){
                proprietarios.add(pessoa);
            }
        }

        return proprietarios;
    }
}
