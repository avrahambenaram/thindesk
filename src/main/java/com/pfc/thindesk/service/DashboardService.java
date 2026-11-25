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
        long chamadosAbertos = contar(chamados, "ABERTO");
        long chamadosResolvidos = contar(chamados, "RESOLVIDO");
        long chamadosAndamento = contar(chamados, "EM_ANDAMENTO");

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

    /** Compara o status ignorando caixa e espaços ("Em Andamento", "EM_ANDAMENTO"...). */
    private long contar(List<Chamado> chamados, String status) {
        return chamados.stream()
                .filter(c -> c.getStatus() != null
                        && c.getStatus().trim().replace(' ', '_').equalsIgnoreCase(status))
                .count();
    }
}
