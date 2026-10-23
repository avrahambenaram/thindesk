package com.pfc.thindesk.controller;

import com.pfc.thindesk.dto.CriarChamadoRequest;
import com.pfc.thindesk.dto.ResolverChamadoRequest;
import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamados")
@Tag(name = "Chamados", description = "Abertura, consulta e resolução de chamados técnicos")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abrir chamado", description = "Abre um chamado associado ao usuário autenticado")
    public Chamado abrirChamado(@Valid @RequestBody CriarChamadoRequest request, Authentication authentication) {
        return chamadoService.abrir(request.titulo(), request.descricao(), authentication.getName());
    }

    @GetMapping
    @Operation(summary = "Listar chamados", description = "Lista todos os chamados cadastrados")
    public List<Chamado> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Listar chamados do usuário", description = "Lista os chamados do usuário informado")
    public List<Chamado> listarChamadosDoUsuario(@PathVariable("id") String id) {
        return chamadoService.listarPorUsuario(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar chamado", description = "Consulta os detalhes e o status de um chamado")
    public Chamado consultarChamado(@PathVariable("id") String id) {
        return chamadoService.buscarPorId(id);
    }

    @PutMapping("/{id}/resolver")
    @Operation(summary = "Resolver chamado", description = "Registra a solução e altera o status para RESOLVIDO")
    public Chamado resolverChamado(@PathVariable("id") String id, @Valid @RequestBody ResolverChamadoRequest request,
            Authentication authentication) {
        return chamadoService.resolver(id, request.solucao(), authentication.getName());
    }
}
