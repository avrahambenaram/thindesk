package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.TipoChamado;
import com.pfc.thindesk.service.TipoChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-chamado")
@CrossOrigin(origins = "*")
public class TipoChamadoRestController {

    @Autowired
    private TipoChamadoService tipoChamadoService;

    @GetMapping
    public List<TipoChamado> listar() {
        return tipoChamadoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoChamado> obter(@PathVariable String id) {
        TipoChamado tipo = tipoChamadoService.obterPorId(id);
        return tipo != null ? ResponseEntity.ok(tipo) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TipoChamado> criar(@RequestBody TipoChamado tipo) {
        return ResponseEntity.ok(tipoChamadoService.salvar(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoChamado> atualizar(@PathVariable String id, @RequestBody TipoChamado tipo) {
        if (tipoChamadoService.obterPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        tipo.setId(id);
        return ResponseEntity.ok(tipoChamadoService.salvar(tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        tipoChamadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
