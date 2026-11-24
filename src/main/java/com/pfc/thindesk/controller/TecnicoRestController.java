package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "*")
public class TecnicoRestController {

    @Autowired
    private TecnicoService tecnicoService;

    @GetMapping
    public List<Tecnico> listar() {
        return tecnicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> obter(@PathVariable String id) {
        Tecnico tecnico = tecnicoService.obterPorId(id);
        return tecnico != null ? ResponseEntity.ok(tecnico) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Tecnico> criar(@RequestBody Tecnico tecnico) {
        return ResponseEntity.ok(tecnicoService.salvar(tecnico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> atualizar(@PathVariable String id, @RequestBody Tecnico tecnico) {
        if (tecnicoService.obterPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        tecnico.setId(id);
        return ResponseEntity.ok(tecnicoService.salvar(tecnico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
