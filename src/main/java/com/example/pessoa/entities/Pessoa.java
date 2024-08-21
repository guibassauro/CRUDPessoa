package com.example.pessoa.entities;

import java.util.List;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String dataDeNascimento;

    @Column(nullable = false, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "pessoa")
    @JsonIgnoreProperties("pessoa")
    private List<Endereço> enderecos;

    @OneToOne
    @JsonIgnoreProperties("pessoa")
    private Endereço enderecoFavorito;


    public int calculaIdade(){
        LocalDate hoje = LocalDate.now();
        LocalDate dataNascimento = LocalDate.parse(this.getDataDeNascimento());

        int idade;

        idade = hoje.getYear() - dataNascimento.getYear();

        if(dataNascimento.getMonthValue() > hoje.getMonthValue()){
            idade--;
        } else if(dataNascimento.getMonthValue() == hoje.getMonthValue() && hoje.getDayOfMonth() < dataNascimento.getDayOfMonth()){
            idade--;
        }

        return idade;
    }
}
