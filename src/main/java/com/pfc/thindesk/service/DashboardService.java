package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.repository.ChamadoRepository;
import com.pfc.thindesk.repository.ClienteRepository;
import com.pfc.thindesk.repository.TecnicoRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    public DashboardService(ChamadoRepository chamadoRepository,
                           ClienteRepository clienteRepository,
                           TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new HashMap<>();

        List<Chamado> chamados = chamadoRepository.findAll();

        long totalChamados = chamados.size();
        long chamadosAbertos = chamados.stream().filter(c -> "Aberto".equals(c.getStatus())).count();
        long chamadosResolvidos = chamados.stream().filter(c -> "Resolvido".equals(c.getStatus())).count();
        long chamadosAndamento = chamados.stream().filter(c -> "Em Andamento".equals(c.getStatus())).count();

        long totalClientes = clienteRepository.count();
        long totalTecnicos = tecnicoRepository.count();

        stats.put("totalChamados", totalChamados);
        stats.put("chamadosAbertos", chamadosAbertos);
        stats.put("chamadosResolvidos", chamadosResolvidos);
        stats.put("chamadosAndamento", chamadosAndamento);
        stats.put("totalClientes", totalClientes);
        stats.put("totalTecnicos", totalTecnicos);
        stats.put("percentualResolucao", totalChamados > 0
                ? Math.round((double) chamadosResolvidos / totalChamados * 100)
                : 0);

        return stats;
    }
}
