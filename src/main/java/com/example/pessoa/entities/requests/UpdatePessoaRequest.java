package com.example.pessoa.entities.requests;

import java.util.List;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePessoaRequest {
    
    String nome;
    String dataDeNascimento;
    String cpf;
    
    @NonNull
    List<Long> enderecos_id;

}
