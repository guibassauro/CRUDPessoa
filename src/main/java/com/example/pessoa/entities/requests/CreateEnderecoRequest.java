package com.example.pessoa.entities.requests;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnderecoRequest {
    
    String rua;
    String numero;
    String bairro;
    String cidade;
    String estado;
    String cep;
    @NonNull
    Long pessoa_id;
}
