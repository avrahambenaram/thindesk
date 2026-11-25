package com.pfc.thindesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pfc.thindesk.entity.Cliente;
import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.HorarioAtendimento;
import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.entity.TipoChamado;
import com.pfc.thindesk.entity.Recurso;
import com.pfc.thindesk.service.ClienteService;
import com.pfc.thindesk.service.ChamadoService;
import com.pfc.thindesk.service.HorarioAtendimentoService;
import com.pfc.thindesk.service.TecnicoService;
import com.pfc.thindesk.service.TipoChamadoService;
import com.pfc.thindesk.service.RecursoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ChamadoService chamadoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private HorarioAtendimentoService horarioAtendimentoService;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private TipoChamadoService tipoChamadoService;
    @Autowired
    private RecursoService recursoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("layout");
        String fragment = "home :: content";
        log.info("Carregando fragmento: {}", fragment); // Log para depuração
        modelAndView.addObject("content", fragment);
        return modelAndView;
    }

    @GetMapping("/chamados")
    public String chamados(Model model) {
        List<Chamado> chamados = chamadoService.listarChamados();
        model.addAttribute("chamados", chamados); // Passa a lista para o modelo
        String fragment = "chamados :: content";
        log.info("Carregando fragmento: {}", fragment); // Log para depuração
        model.addAttribute("content", fragment);
        return "chamados";
    }

    @GetMapping("/clientes")
    public String clientes(Model model) {
        List<Cliente> clientes = clienteService.listarClientes();
        model.addAttribute("clientes", clientes);
        String fragment = "clientes :: content";
        log.info("Carregando fragmento: {}", fragment); // Log para depuração
        model.addAttribute("content", fragment);
        return "clientes";
    }
    
    @GetMapping("/ajustes-horarios")
    public String horarios(Model model) {
        List<HorarioAtendimento> horarios = horarioAtendimentoService.listarTodos();
        model.addAttribute("ajustesHorarios", horarios);
        String fragment = "ajustes-horarios :: content";
        log.info("Carregando fragmento: {}", fragment);
        model.addAttribute("content", fragment);
        return "ajustes-horarios";
    }

    @GetMapping("/abrir-chamado")
    public String abrirChamado(Model model) {
        List<TipoChamado> tiposAtivos = tipoChamadoService.listarTodos().stream()
                .filter(TipoChamado::isAtivo)
                .toList();
        model.addAttribute("tiposAtivos", tiposAtivos);
        return "abrir-chamado";
    }

    @GetMapping("/tecnicos")
    public String tecnicos(Model model) {
        List<Tecnico> tecnicos = tecnicoService.listarTodos();
        model.addAttribute("tecnicos", tecnicos);
        String fragment = "tecnicos :: content";
        log.info("Carregando fragmento: {}", fragment);
        model.addAttribute("content", fragment);
        return "tecnicos";
    }

    @GetMapping("/tipo-chamado")
    public String tipoChamado(Model model) {
        List<TipoChamado> tiposChamado = tipoChamadoService.listarTodos();
        model.addAttribute("tiposChamado", tiposChamado);
        String fragment = "tipo-chamado :: content";
        log.info("Carregando fragmento: {}", fragment);
        model.addAttribute("content", fragment);
        return "tipo-chamado";
    }

    @GetMapping("/recursos")
    public String recursos(Model model) {
        List<Recurso> recursos = recursoService.listarTodos();
        model.addAttribute("recursos", recursos);
        String fragment = "recursos :: content";
        log.info("Carregando fragmento: {}", fragment);
        model.addAttribute("content", fragment);
        return "recursos";
    }

}
