package com.example.pessoa.entities.requests;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFavoritoRequest {
    
    @NonNull
    Long enderecoFavorito_id;
}
