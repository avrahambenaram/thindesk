package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.TipoChamado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoChamadoRepository extends MongoRepository<TipoChamado, String> {
}
