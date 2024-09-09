package com.example.pessoa.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarEnderecoRequest {
    
    String rua;
    String numero;
    String bairro;
    String cidade;
    String estado;
    String cep;
}
