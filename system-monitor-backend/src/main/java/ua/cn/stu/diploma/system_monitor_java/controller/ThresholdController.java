package ua.cn.stu.diploma.system_monitor_java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.diploma.system_monitor_java.dto.ThresholdRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.ThresholdResponse;
import ua.cn.stu.diploma.system_monitor_java.service.ThresholdService;

@RestController
@RequestMapping("/api/thresholds")
@RequiredArgsConstructor
public class ThresholdController {

    private final ThresholdService thresholdService;

    @GetMapping
    public ResponseEntity<ThresholdResponse> get(@AuthenticationPrincipal String username,
                                                 @RequestParam Long agentId ) {
        return ResponseEntity.ok(thresholdService.getThreshold(username, agentId));
    }

    @PutMapping
    public ResponseEntity<ThresholdResponse> update(@AuthenticationPrincipal String username,
                                                    @RequestParam Long agentId,
                                                    @RequestBody ThresholdRequest req) {
        return ResponseEntity.ok(thresholdService.updateThreshold(username, agentId, req));
    }
}
