package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.EventoChamado;
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
    private static final String STATUS_EM_ANDAMENTO = "Em Andamento";
    private static final String STATUS_RESOLVIDO = "Resolvido";
    private static final String STATUS_CANCELADO = "Cancelado";

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
        chamado.getHistorico().add(new EventoChamado("ABERTURA", usuario, chamado.getDescricao(),
                chamado.getDataAbertura()));
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

    public Chamado colocarEmAndamento(String id, String username) {
        Tecnico tecnico = tecnicoDoLogin(username);
        Chamado chamado = buscarPorId(id);
        if (STATUS_RESOLVIDO.equalsIgnoreCase(chamado.getStatus()) || chamado.getDataResolucao() != null) {
            throw new ChamadoJaResolvidoException(id);
        }
        LocalDateTime agora = LocalDateTime.now();
        chamado.setStatus(STATUS_EM_ANDAMENTO);
        chamado.setTecnicoId(tecnico.getId());
        chamado.setTecnico(tecnico.getNome());
        chamado.getHistorico().add(new EventoChamado("EM_ANDAMENTO", tecnico.getNome(), "Iniciado atendimento", agora));
        return chamadoRepository.save(chamado);
    }

    /** Só técnicos ativos resolvem chamados. O técnico é identificado pelo login. O motivo é obrigatório. */
    public Chamado resolver(String id, String motivo, String username) {
        Tecnico tecnico = tecnicoDoLogin(username);
        Chamado chamado = buscarPorId(id);
        if (STATUS_RESOLVIDO.equalsIgnoreCase(chamado.getStatus()) || chamado.getDataResolucao() != null) {
            throw new ChamadoJaResolvidoException(id);
        }
        String texto = motivo.trim();
        LocalDateTime agora = LocalDateTime.now();
        chamado.setSolucao(texto);
        chamado.setStatus(STATUS_RESOLVIDO);
        chamado.setTecnicoId(tecnico.getId());
        chamado.setTecnico(tecnico.getNome());
        chamado.setDataResolucao(agora);
        chamado.getHistorico().add(new EventoChamado("RESOLUCAO", tecnico.getNome(), texto, agora));
        return chamadoRepository.save(chamado);
    }

    /** Admin edita título, descrição e tipo. Chamado resolvido ou cancelado não pode ser alterado. */
    public Chamado editar(String id, String titulo, String descricao, String tipoId, String username) {
        Chamado chamado = buscarPorId(id);
        validarAlteravel(chamado);
        TipoChamado tipo = tipoChamadoService.obterPorId(tipoId);
        if (tipo == null) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Tipo de chamado inválido");
        }
        chamado.setTitulo(titulo.trim());
        chamado.setDescricao(descricao.trim());
        chamado.setTipoId(tipo.getId());
        chamado.setTipo(tipo.getDescricao());
        chamado.getHistorico().add(new EventoChamado("EDICAO", username, "Chamado editado", LocalDateTime.now()));
        return chamadoRepository.save(chamado);
    }

    public Chamado cancelar(String id, String username) {
        Chamado chamado = buscarPorId(id);
        validarAlteravel(chamado);
        chamado.setStatus(STATUS_CANCELADO);
        chamado.getHistorico().add(new EventoChamado("CANCELAMENTO", username, "Chamado cancelado", LocalDateTime.now()));
        return chamadoRepository.save(chamado);
    }

    private void validarAlteravel(Chamado chamado) {
        if (STATUS_RESOLVIDO.equalsIgnoreCase(chamado.getStatus()) || chamado.getDataResolucao() != null) {
            throw new ChamadoJaResolvidoException(chamado.getId());
        }
        if (STATUS_CANCELADO.equalsIgnoreCase(chamado.getStatus())) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Chamado já cancelado");
        }
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
