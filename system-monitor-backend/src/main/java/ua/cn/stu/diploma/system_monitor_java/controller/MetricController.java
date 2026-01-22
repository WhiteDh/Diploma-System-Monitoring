package ua.cn.stu.diploma.system_monitor_java.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.diploma.system_monitor_java.dto.MetricRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.MetricResponse;
import ua.cn.stu.diploma.system_monitor_java.service.AgentService;
import ua.cn.stu.diploma.system_monitor_java.service.MetricService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;
    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<Void> push(@AuthenticationPrincipal String username,
                                     @Valid @RequestBody MetricRequest req) {
        metricService.saveMetric(username, req);
        agentService.updateLastSeen(req.getAgentId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<MetricResponse>> history(
            @AuthenticationPrincipal String username,
            @RequestParam Long agentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        return ResponseEntity.ok(metricService.getHistory(agentId, from, to, username));
    }

}
