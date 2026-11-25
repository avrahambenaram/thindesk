package com.pfc.thindesk.controller;

import com.pfc.thindesk.dto.ClienteRequest;
import com.pfc.thindesk.entity.Cliente;
import com.pfc.thindesk.service.ClienteService;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public Cliente obter(@PathVariable("id") String id) {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    public Cliente novoCliente(@Valid @RequestBody ClienteRequest request) {
        return clienteService.novoCliente(request);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable("id") String id, @Valid @RequestBody ClienteRequest request) {
        return clienteService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") String id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
