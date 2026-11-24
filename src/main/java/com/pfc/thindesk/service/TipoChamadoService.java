package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.TipoChamado;
import com.pfc.thindesk.repository.TipoChamadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoChamadoService {

    @Autowired
    private TipoChamadoRepository tipoChamadoRepository;

    public List<TipoChamado> listarTodos() {
        return tipoChamadoRepository.findAll();
    }

    public TipoChamado obterPorId(String id) {
        return tipoChamadoRepository.findById(id).orElse(null);
    }

    public TipoChamado salvar(TipoChamado tipoChamado) {
        return tipoChamadoRepository.save(tipoChamado);
    }

    public void deletar(String id) {
        tipoChamadoRepository.deleteById(id);
    }
}
