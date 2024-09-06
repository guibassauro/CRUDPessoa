package com.example.pessoa.services.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.pessoa.entities.Endereço;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.CreateEnderecoRequest;
import com.example.pessoa.entities.requests.UpdateEnderecoRequest;
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
    public ResponseEntity<Object> criaEndereco(CreateEnderecoRequest criaEndereco){
        
        Endereço novoEndereco = new Endereço(
            null,
            criaEndereco.getRua(),
            criaEndereco.getNumero(),
            criaEndereco.getCidade(),
            criaEndereco.getEstado(),
            criaEndereco.getCep(),
            null
        );

        endereçoRepository.save(novoEndereco);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(novoEndereco);
    }

    @Override
    public ResponseEntity<Object> atualizaEndereco(Long id, UpdateEnderecoRequest atualizaEndereco){
        if(!confereEndereco(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("O endereço " + id + " não foi encontrado");
        }

        if(atualizaEndereco.getPessoa_id() != null && !conferePessoa(atualizaEndereco.getPessoa_id())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("A pessoa " + atualizaEndereco.getPessoa_id() + " não foi encontrada");
        }

        Endereço enderecoAtualizado = endereçoRepository.findById(id).get();

        enderecoAtualizado.setRua(atualizaEndereco.getRua());
        enderecoAtualizado.setNumero(atualizaEndereco.getNumero());
        enderecoAtualizado.setCidade(atualizaEndereco.getCidade());
        enderecoAtualizado.setEstado(atualizaEndereco.getEstado());
        enderecoAtualizado.setCep(atualizaEndereco.getCep());

        if(atualizaEndereco.getPessoa_id() == null){
            enderecoAtualizado.setPessoa(null);
        } else{
            Pessoa pessoaAtualizada = pessoaRepository.findById(atualizaEndereco.getPessoa_id()).get();
            enderecoAtualizado.setPessoa(pessoaAtualizada);
        }

        endereçoRepository.save(enderecoAtualizado);

        return ResponseEntity.ok().body(enderecoAtualizado);

    }

    public ResponseEntity<Object> deletaEndereco(Long id){
        if(!confereEndereco(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("O endereço " + id + " não foi encontrado");
        }

        if(endereçoRepository.findById(id).get().getPessoa() == null){
            endereçoRepository.deleteById(id);
            return ResponseEntity.ok().body("Endereço deletado");
        }

        if(endereçoRepository.findById(id).get().getPessoa().getEnderecos().size() < 2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Se você deletar este endereço " +
                                 endereçoRepository.findById(id).get().getPessoa().getNome() + 
                                 " vai ficar sem endereços");
        }

        if(endereçoRepository.findById(id).get().getPessoa().getEnderecoFavorito().getId() == id){
            endereçoRepository.findById(id).get().getPessoa().setEnderecoFavorito(null);
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
}
