package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Recurso;
import com.pfc.thindesk.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public List<Recurso> listarTodos() {
        return recursoRepository.findAll();
    }

    public Recurso obterPorId(String id) {
        return recursoRepository.findById(id).orElse(null);
    }

    public Recurso salvar(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    public void deletar(String id) {
        recursoRepository.deleteById(id);
    }
}
