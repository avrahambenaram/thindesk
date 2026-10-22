package com.pfc.thindesk.controller;

import com.pfc.thindesk.dto.CriarChamadoRequest;
import com.pfc.thindesk.dto.ResolverChamadoRequest;
import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.service.ChamadoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chamado abrirChamado(@Valid @RequestBody CriarChamadoRequest request, Authentication authentication) {
        return chamadoService.abrir(request.titulo(), request.descricao(), authentication.getName());
    }

    @GetMapping
    public List<Chamado> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/usuario/{id}")
    public List<Chamado> listarChamadosDoUsuario(@PathVariable String id) {
        return chamadoService.listarPorUsuario(id);
    }

    @GetMapping("/{id}")
    public Chamado consultarChamado(@PathVariable String id) {
        return chamadoService.buscarPorId(id);
    }

    @PutMapping("/{id}/resolver")
    public Chamado resolverChamado(@PathVariable String id, @Valid @RequestBody ResolverChamadoRequest request,
            Authentication authentication) {
        return chamadoService.resolver(id, request.solucao(), authentication.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizarChamado(@PathVariable String id, @RequestBody Chamado chamadoAtualizado) {
        Chamado chamado = chamadoService.atualizarChamado(id, chamadoAtualizado);
        return ResponseEntity.ok(chamado);
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarChamado(@PathVariable String id, @ModelAttribute Chamado chamadoAtualizado, Model model) {
        chamadoService.atualizarChamado(id, chamadoAtualizado);
        return "redirect:/chamados";
    }
}
