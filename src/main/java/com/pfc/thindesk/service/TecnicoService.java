package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico obterPorId(String id) {
        return tecnicoRepository.findById(id).orElse(null);
    }

    public Tecnico salvar(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public void deletar(String id) {
        tecnicoRepository.deleteById(id);
    }
}
