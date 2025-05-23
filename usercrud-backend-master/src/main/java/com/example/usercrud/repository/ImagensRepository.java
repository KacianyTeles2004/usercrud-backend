package com.example.usercrud.repository;

import com.example.usercrud.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagensRepository extends JpaRepository<ImagemProduto, Long> {
}
