package com.pfc.thindesk.service;

import com.pfc.thindesk.dto.ClienteRequest;
import com.pfc.thindesk.entity.Cliente;
import com.pfc.thindesk.exception.RegraDeNegocioException;
import com.pfc.thindesk.repository.ClienteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(String id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public Cliente novoCliente(ClienteRequest request) {
        Cliente cliente = new Cliente();
        aplicar(cliente, request);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(String id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);
        aplicar(cliente, request);
        return clienteRepository.save(cliente);
    }

    public void deletar(String id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }

    private void aplicar(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.nome().trim());
        cliente.setTelefone(request.telefone());
        cliente.setSetor(request.setor());
    }
}
