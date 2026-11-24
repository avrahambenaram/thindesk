package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Recurso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepository extends MongoRepository<Recurso, String> {
}
