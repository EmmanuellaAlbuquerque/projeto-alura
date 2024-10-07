package br.com.alura.ProjetoAlura.user;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndRole(String email, Role role);

    Optional<User> findByEmail(String email);
}
