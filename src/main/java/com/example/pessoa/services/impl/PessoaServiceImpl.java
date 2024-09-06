package com.example.pessoa.services.impl;

import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.pessoa.entities.Endereço;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.CreatePessoaRequest;
import com.example.pessoa.entities.requests.UpdateFavoritoRequest;
import com.example.pessoa.entities.requests.UpdatePessoaRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import com.example.pessoa.services.PessoaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PessoaServiceImpl implements PessoaService {
    final PessoaRepository pessoaRepository;
    final EndereçoRepository endereçoRepository;

    @Override 
    public ResponseEntity<Object> achaTodos(){
        return ResponseEntity.ok().body(pessoaRepository.findAll());
    }
    

    @Override
    public ResponseEntity<Object> pegaIdadeDaPessoaPorId(Long id){
        if(!conferePessoa(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Pessoa " + id + " não encontrada");
        }

        int idade = pessoaRepository.findById(id).get().calculaIdade();

        return ResponseEntity.ok().body(pessoaRepository.findById(id).get().getNome() + " tem " + idade + " anos");
    }

    @Override
    public ResponseEntity<Object> criaPessoa(CreatePessoaRequest criaPessoa){
        
        for(Long endereco_id : criaPessoa.getEnderecos_id()){
            if(!confereEndereco(endereco_id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Endereço " + endereco_id + " não encontrado");
            }
        }

        List<Endereço> criaPessoaEnderecos = endereçoRepository.findAllById(criaPessoa.getEnderecos_id());

        Pessoa novaPessoa = new Pessoa(
            null,
            criaPessoa.getNome(),
            criaPessoa.getDataDeNascimento(),
            criaPessoa.getCpf(),
            criaPessoaEnderecos,
            null
        );

        criaPessoa.getEnderecos_id().forEach(endereco_id -> {
            endereçoRepository.findById(endereco_id).get().addPessoa(novaPessoa);
        });

        pessoaRepository.save(novaPessoa);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(novaPessoa);
    }
    
    @Override
    public ResponseEntity<Object> atualizaPessoa(Long id, UpdatePessoaRequest atualizaPessoa){
        if(!conferePessoa(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Pessoa " + id + " não encontrada.");
        }

        for(Long endereco_id : atualizaPessoa.getEnderecos_id()){
            if(!confereEndereco(endereco_id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Endereço " + endereco_id + " não econtrado.");
            }
        }

        List<Endereço> atualizaEndereços = endereçoRepository.findAllById(atualizaPessoa.getEnderecos_id());

        if(atualizaEndereços.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("A pessoa precisa de pelo menos um endereço");
        }

        Pessoa pessoAtualizada = pessoaRepository.findById(id).get();
        Endereço enderecoFavorito = pessoaRepository.findById(id).get().getEnderecoFavorito();

        pessoaRepository.findById(id).get().getEnderecos().forEach(endereco -> {
            endereco.setPessoa(null);
        });

        pessoAtualizada.setNome(atualizaPessoa.getNome());
        pessoAtualizada.setCpf(atualizaPessoa.getCpf());
        pessoAtualizada.setDataDeNascimento(atualizaPessoa.getDataDeNascimento());
        pessoAtualizada.setEnderecos(atualizaEndereços);
        pessoAtualizada.setEnderecoFavorito(enderecoFavorito);

        atualizaEndereços.forEach(endereco -> {
            endereco.addPessoa(pessoAtualizada);
        });

        pessoaRepository.save(pessoAtualizada);

        return ResponseEntity.ok().body(pessoAtualizada);
    }

    @Override
    public ResponseEntity<Object> adicionaEnderecoFavoritoParaPessoa(Long id, UpdateFavoritoRequest atualizaFavorito){
        if(!conferePessoa(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Pessoa " + id + " não encontrada");
        }

        if(!confereEndereco(atualizaFavorito.getEnderecoFavorito_id())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Endereço " + atualizaFavorito.getEnderecoFavorito_id() + " não econtrado.");
        }

        Optional<Endereço> enderecoFavorito = endereçoRepository.findById(atualizaFavorito.getEnderecoFavorito_id());

        if(!pessoaRepository.findById(id).get().getEnderecos().contains(enderecoFavorito.get())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Uma pessoa só pode favoritar um endereço que já tem");
        }

        Pessoa atualizaPessoa = pessoaRepository.findById(id).get();

        atualizaPessoa.setEnderecoFavorito(enderecoFavorito.get());

        return ResponseEntity.ok().body(pessoaRepository.save(atualizaPessoa));
    }

    @Override
    public ResponseEntity<Object> deletaPessoa(Long id){
        if(!conferePessoa(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Pessoa " + id + " não econtrada.");
        }

        pessoaRepository.findById(id).get().setEnderecoFavorito(null);

        List<Endereço> deletaEnderecosDaPessoa = pessoaRepository.findById(id).get().getEnderecos();

        endereçoRepository.deleteAll(deletaEnderecosDaPessoa);
        pessoaRepository.deleteById(id);

        return ResponseEntity.ok().body("Pessoa deletada");
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
