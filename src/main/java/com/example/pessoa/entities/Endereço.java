package com.example.pessoa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endereco")
public class Endereço {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String rua;

    @Column
    private String numero;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column
    private String cep;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    @JsonIgnoreProperties("enderecos")
    private Pessoa pessoa;
    
    public void addPessoa(final Pessoa pessoa){
        this.pessoa = pessoa;
    }

}
