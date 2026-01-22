package ua.cn.stu.diploma.system_monitor_java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.diploma.system_monitor_java.dto.AgentRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.AgentResponse;
import ua.cn.stu.diploma.system_monitor_java.service.AgentService;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/register")
    public ResponseEntity<AgentResponse> register(Authentication authentication,
                                                  @RequestBody AgentRequest req) {
        String username = authentication.getName();
        return ResponseEntity.ok(agentService.registerAgent(username, req));
    }

    @GetMapping
    public ResponseEntity<List<AgentResponse>> list(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(agentService.listAgents(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentResponse> getAgentById(@PathVariable Long id,
                                                      Authentication authentication) {
        String username = authentication.getName();
        AgentResponse response = agentService.getAgentByIdForUser(id, username);
        return ResponseEntity.ok(response);
    }
}
