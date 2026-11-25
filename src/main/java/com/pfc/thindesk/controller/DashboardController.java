package com.pfc.thindesk.controller;

import com.pfc.thindesk.service.DashboardService;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@PreAuthorize("hasAnyRole('ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(Model model) {
        Map<String, Object> stats = dashboardService.obterEstatisticas();
        model.addAllAttributes(stats);
        model.addAttribute("content", "dashboard :: content");
        return "dashboard";
    }
}
