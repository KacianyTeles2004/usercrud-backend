package com.example.usercrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.usercrud.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE (u.cpf = :identificador OR u.email = :identificador) AND u.senha = :senha")
    User findByCpfOrEmailAndSenha(@Param("identificador") String identificador, @Param("senha") String senha);
}
