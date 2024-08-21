package com.example.pessoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pessoa.entities.Endereço;

@Repository
public interface EndereçoRepository extends JpaRepository<Endereço, Long> {
    
}
