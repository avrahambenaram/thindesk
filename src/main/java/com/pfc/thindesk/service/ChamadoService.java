package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.exception.ChamadoJaResolvidoException;
import com.pfc.thindesk.exception.ChamadoNaoEncontradoException;
import com.pfc.thindesk.repository.ChamadoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    private static final String STATUS_ABERTO = "ABERTO";
    private static final String STATUS_RESOLVIDO = "RESOLVIDO";

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public Chamado abrir(String titulo, String descricao, String usuario) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo.trim());
        chamado.setDescricao(descricao.trim());
        chamado.setStatus(STATUS_ABERTO);
        chamado.setUsuario(usuario);
        chamado.setDataAbertura(LocalDateTime.now());
        return chamadoRepository.save(chamado);
    }

    public List<Chamado> listarChamados() {
        return chamadoRepository.findAll();
    }

    public List<Chamado> listarPorUsuario(String usuario) {
        return chamadoRepository.findByUsuarioOrderByDataAberturaDesc(usuario);
    }

    public Chamado buscarPorId(String id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ChamadoNaoEncontradoException(id));
    }

    public Chamado resolver(String id, String solucao, String tecnico) {
        Chamado chamado = buscarPorId(id);
        if (STATUS_RESOLVIDO.equalsIgnoreCase(chamado.getStatus()) || chamado.getDataResolucao() != null) {
            throw new ChamadoJaResolvidoException(id);
        }
        chamado.setSolucao(solucao.trim());
        chamado.setStatus(STATUS_RESOLVIDO);
        chamado.setTecnico(tecnico);
        chamado.setDataResolucao(LocalDateTime.now());
        return chamadoRepository.save(chamado);
    }
}
