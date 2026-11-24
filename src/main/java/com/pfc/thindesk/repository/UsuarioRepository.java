package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Usuario;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
