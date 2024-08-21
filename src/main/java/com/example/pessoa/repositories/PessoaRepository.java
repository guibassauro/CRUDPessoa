package com.example.pessoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import com.example.pessoa.entities.Pessoa;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
