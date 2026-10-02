package side.financialmanagementapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import side.financialmanagementapi.dto.response.DashboardResponse;
import side.financialmanagementapi.dto.response.MonthlyDashboardResponse;
import side.financialmanagementapi.service.DashboardService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/dashboard")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/total")
    public ResponseEntity<?> getDashboard(){

       ResponseEntity<?> resposta = dashboardService.dashboard();

        return ResponseEntity.ok(resposta).getBody();
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyDashboardResponse>> getMonthlyDashboard() {

        return ResponseEntity.ok(
                dashboardService.monthlyDashboard()
        );
    }

}
