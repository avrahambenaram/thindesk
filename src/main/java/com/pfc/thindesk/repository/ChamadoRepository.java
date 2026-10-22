package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Chamado;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChamadoRepository extends MongoRepository<Chamado, String> {
    List<Chamado> findByUsuarioOrderByDataAberturaDesc(String usuario);
}
