package com.pfc.thindesk.service;

import com.pfc.thindesk.dto.CadastrarTecnicoRequest;
import com.pfc.thindesk.dto.EditarTecnicoRequest;
import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.exception.RegraDeNegocioException;
import com.pfc.thindesk.repository.TecnicoRepository;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public TecnicoService(TecnicoRepository tecnicoRepository,
                          UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService) {
        this.tecnicoRepository = tecnicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico obterPorId(String id) {
        return tecnicoRepository.findById(id).orElse(null);
    }

    /** Cria o técnico e o login dele (perfil TECNICO), já vinculados. */
    public Tecnico cadastrar(CadastrarTecnicoRequest request) {
        Usuario login = usuarioService.criar(request.username(), request.senha(),
                List.of(UsuarioService.ROLE_TECNICO));

        Tecnico tecnico = new Tecnico();
        tecnico.setNome(request.nome().trim());
        tecnico.setEmail(request.email());
        tecnico.setTelefone(request.telefone());
        tecnico.setEspecialidade(request.especialidade());
        tecnico.setAtivo(true);
        tecnico.setUsuarioId(login.getId());
        return tecnicoRepository.save(tecnico);
    }

    /** Edita os dados do técnico. Desativar o técnico também bloqueia o login. */
    public Tecnico editar(String id, EditarTecnicoRequest request) {
        Tecnico tecnico = obterPorId(id);
        if (tecnico == null) {
            throw new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Técnico não encontrado");
        }
        tecnico.setNome(request.nome().trim());
        tecnico.setEmail(request.email());
        tecnico.setTelefone(request.telefone());
        tecnico.setEspecialidade(request.especialidade());
        tecnico.setAtivo(request.ativo());
        if (tecnico.getUsuarioId() != null) {
            usuarioService.definirAtivo(tecnico.getUsuarioId(), request.ativo());
        }
        return tecnicoRepository.save(tecnico);
    }

    /** Remove o técnico e o login vinculado a ele. */
    public void deletar(String id) {
        Tecnico tecnico = obterPorId(id);
        if (tecnico != null && tecnico.getUsuarioId() != null) {
            usuarioRepository.deleteById(tecnico.getUsuarioId());
        }
        tecnicoRepository.deleteById(id);
    }
}
