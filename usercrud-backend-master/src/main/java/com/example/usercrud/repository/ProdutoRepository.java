package com.example.usercrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.usercrud.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}

