package com.example.pessoa.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarEnderecoRequest {
    
    String rua;
    String numero;
    String cidade;
    String estado;
    String cep;

}
