package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.entity.TipoChamado;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.exception.ChamadoJaResolvidoException;
import com.pfc.thindesk.exception.ChamadoNaoEncontradoException;
import com.pfc.thindesk.exception.RegraDeNegocioException;
import com.pfc.thindesk.repository.ChamadoRepository;
import com.pfc.thindesk.repository.TecnicoRepository;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    private static final String STATUS_ABERTO = "ABERTO";
    private static final String STATUS_RESOLVIDO = "RESOLVIDO";

    private final ChamadoRepository chamadoRepository;
    private final TipoChamadoService tipoChamadoService;
    private final UsuarioRepository usuarioRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          TipoChamadoService tipoChamadoService,
                          UsuarioRepository usuarioRepository,
                          TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.tipoChamadoService = tipoChamadoService;
        this.usuarioRepository = usuarioRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    /** Qualquer usuário logado abre o chamado, escolhendo um tipo ativo cadastrado. */
    public Chamado abrir(String titulo, String descricao, String tipoId, String usuario) {
        TipoChamado tipo = tipoChamadoService.obterPorId(tipoId);
        if (tipo == null || !tipo.isAtivo()) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Tipo de chamado inválido ou inativo");
        }

        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo.trim());
        chamado.setDescricao(descricao.trim());
        chamado.setTipoId(tipo.getId());
        chamado.setTipo(tipo.getDescricao());
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

    /** Só técnicos ativos resolvem chamados. O técnico é identificado pelo login. */
    public Chamado resolver(String id, String solucao, String username) {
        Tecnico tecnico = tecnicoDoLogin(username);
        Chamado chamado = buscarPorId(id);
        if (STATUS_RESOLVIDO.equalsIgnoreCase(chamado.getStatus()) || chamado.getDataResolucao() != null) {
            throw new ChamadoJaResolvidoException(id);
        }
        chamado.setSolucao(solucao.trim());
        chamado.setStatus(STATUS_RESOLVIDO);
        chamado.setTecnicoId(tecnico.getId());
        chamado.setTecnico(tecnico.getNome());
        chamado.setDataResolucao(LocalDateTime.now());
        return chamadoRepository.save(chamado);
    }

    private Tecnico tecnicoDoLogin(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        Tecnico tecnico = usuario == null ? null
                : tecnicoRepository.findByUsuarioId(usuario.getId()).orElse(null);
        if (tecnico == null) {
            throw new RegraDeNegocioException(HttpStatus.FORBIDDEN,
                    "Apenas técnicos cadastrados podem resolver chamados");
        }
        if (!tecnico.isAtivo()) {
            throw new RegraDeNegocioException(HttpStatus.FORBIDDEN, "Técnico inativo");
        }
        return tecnico;
    }
}
