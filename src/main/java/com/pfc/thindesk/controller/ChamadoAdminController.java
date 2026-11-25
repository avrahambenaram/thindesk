package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.TipoChamado;
import com.pfc.thindesk.service.ChamadoService;
import com.pfc.thindesk.service.TipoChamadoService;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Telas de edição e cancelamento de chamados (somente admin, ver SecurityConfig). */
@Controller
public class ChamadoAdminController {

    private final ChamadoService chamadoService;
    private final TipoChamadoService tipoChamadoService;

    public ChamadoAdminController(ChamadoService chamadoService, TipoChamadoService tipoChamadoService) {
        this.chamadoService = chamadoService;
        this.tipoChamadoService = tipoChamadoService;
    }

    @GetMapping("/chamados/atualizar/{id}")
    public String formEditar(@PathVariable("id") String id, Model model) {
        List<TipoChamado> tipos = tipoChamadoService.listarTodos().stream()
                .filter(TipoChamado::isAtivo)
                .toList();
        model.addAttribute("chamado", chamadoService.buscarPorId(id));
        model.addAttribute("tipos", tipos);
        return "chamados/atualizar";
    }

    @PostMapping("/chamados/atualizar/{id}")
    public String editar(@PathVariable("id") String id,
                         @RequestParam("titulo") String titulo,
                         @RequestParam("descricao") String descricao,
                         @RequestParam("tipoId") String tipoId,
                         Authentication authentication) {
        chamadoService.editar(id, titulo, descricao, tipoId, authentication.getName());
        return "redirect:/chamados";
    }

    @PostMapping("/chamados/cancelar/{id}")
    public String cancelar(@PathVariable("id") String id, Authentication authentication) {
        chamadoService.cancelar(id, authentication.getName());
        return "redirect:/chamados";
    }
}
