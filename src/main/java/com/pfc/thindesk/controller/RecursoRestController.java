package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Recurso;
import com.pfc.thindesk.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@CrossOrigin(origins = "*")
public class RecursoRestController {

    @Autowired
    private RecursoService recursoService;

    @GetMapping
    public List<Recurso> listar() {
        return recursoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recurso> obter(@PathVariable String id) {
        Recurso recurso = recursoService.obterPorId(id);
        return recurso != null ? ResponseEntity.ok(recurso) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Recurso> criar(@RequestBody Recurso recurso) {
        return ResponseEntity.ok(recursoService.salvar(recurso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recurso> atualizar(@PathVariable String id, @RequestBody Recurso recurso) {
        if (recursoService.obterPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        recurso.setId(id);
        return ResponseEntity.ok(recursoService.salvar(recurso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        recursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
