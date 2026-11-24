package com.pfc.thindesk.controller;

import com.pfc.thindesk.dto.CadastrarTecnicoRequest;
import com.pfc.thindesk.dto.EditarTecnicoRequest;
import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.service.TecnicoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoRestController {

    private final TecnicoService tecnicoService;

    public TecnicoRestController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping
    public List<Tecnico> listar() {
        return tecnicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> obter(@PathVariable("id") String id) {
        Tecnico tecnico = tecnicoService.obterPorId(id);
        return tecnico != null ? ResponseEntity.ok(tecnico) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Tecnico> criar(@Valid @RequestBody CadastrarTecnicoRequest request) {
        return ResponseEntity.ok(tecnicoService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> atualizar(@PathVariable("id") String id,
                                             @Valid @RequestBody EditarTecnicoRequest request) {
        return ResponseEntity.ok(tecnicoService.editar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
