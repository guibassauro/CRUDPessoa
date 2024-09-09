package com.example.pessoa.services.impl;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.pessoa.entities.Endereço;
import com.example.pessoa.entities.Pessoa;
import com.example.pessoa.entities.requests.CriarPessoaRequest;
import com.example.pessoa.entities.requests.AtualizarFavoritoRequest;
import com.example.pessoa.entities.requests.AtualizarPessoaRequest;
import com.example.pessoa.repositories.EndereçoRepository;
import com.example.pessoa.repositories.PessoaRepository;
import com.example.pessoa.services.PessoaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PessoaServiceImpl implements PessoaService {
    final PessoaRepository pessoaRepository;
    final EndereçoRepository endereçoRepository;

    public Page<Pessoa> achaTodos(Pageable pageable){
        return pessoaRepository.findAll(pageable);
    }
    

    @Override
    public ResponseEntity<Object> pegaIdadeDaPessoaPorId(Long id){
        if(!conferePessoa(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Pessoa " + id + " não encontrada");
        }

        int idade = pessoaRepository.findById(id).get().calculaIdade();

        return ResponseEntity.ok().body(pessoaRepository.findById(id).get().getNome() + 
                                        " tem " + idade + " anos");
    }

    @Override
    public ResponseEntity<Object> criaPessoa(CriarPessoaRequest criaPessoa){

        List<Endereço> criaPessoaEnderecos = endereçoRepository.findAllById(criaPessoa.getEnderecos_id());

        if(criaPessoaEnderecos.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Uma pessoa precisa de pelo menos um endereço!");
        }

        /* Solução terrível abaixo */

        for(Pessoa pessoa : pessoaRepository.findAll()){
            for(int i=0; i<pessoa.getEnderecos().size(); i++){
                if(pessoa.getEnderecos().contains(criaPessoaEnderecos.get(i))){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                         .body("Este endereço já tem proprietário!");
                }
            }
        }

        for(Long endereco_id : criaPessoa.getEnderecos_id()){
            if(!confereEndereco(endereco_id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Endereço " + endereco_id + " não encontrado");
            }
        }


        Pessoa novaPessoa = new Pessoa(
            null,
            criaPessoa.getNome(),
            criaPessoa.getDataDeNascimento(),
            criaPessoa.getCpf(),
            criaPessoaEnderecos,
            null
        );

        pessoaRepository.save(novaPessoa);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(novaPessoa);
    }
    
    @Override
    public ResponseEntity<Object> atualizaPessoa(Long id, AtualizarPessoaRequest atualizaPessoa){
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

        Pessoa pessoaAtualizada = pessoaRepository.findById(id).get();
        Endereço enderecoFavorito = pessoaRepository.findById(id).get().getEnderecoFavorito();

        pessoaAtualizada.setNome(atualizaPessoa.getNome());
        pessoaAtualizada.setCpf(atualizaPessoa.getCpf());
        pessoaAtualizada.setDataDeNascimento(atualizaPessoa.getDataDeNascimento());
        pessoaAtualizada.setEnderecos(atualizaEndereços);
        pessoaAtualizada.setEnderecoFavorito(enderecoFavorito);

        pessoaRepository.save(pessoaAtualizada);

        return ResponseEntity.ok().body(pessoaAtualizada);
    }

    @Override
    public ResponseEntity<Object> adicionaEnderecoFavoritoParaPessoa(Long id, AtualizarFavoritoRequest atualizaFavorito){
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

        List<Endereço> deletaEnderecosDaPessoa = pessoaRepository.findById(id).get().getEnderecos();

        pessoaRepository.findById(id).get().setEnderecos(null);
        pessoaRepository.findById(id).get().setEnderecoFavorito(null);

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
