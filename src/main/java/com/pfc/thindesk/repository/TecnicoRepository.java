package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Tecnico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnicoRepository extends MongoRepository<Tecnico, String> {
}
