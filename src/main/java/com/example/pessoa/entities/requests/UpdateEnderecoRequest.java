package com.example.pessoa.entities.requests;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEnderecoRequest {
    
    String rua;
    String numero;
    String cidade;
    String estado;
    String cep;
    @NonNull
    Long pessoa_id;

}
