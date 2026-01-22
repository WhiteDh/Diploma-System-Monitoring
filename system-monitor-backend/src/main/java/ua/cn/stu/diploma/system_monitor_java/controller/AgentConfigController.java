package ua.cn.stu.diploma.system_monitor_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.cn.stu.diploma.system_monitor_java.dto.AgentConfigurationDto;
import ua.cn.stu.diploma.system_monitor_java.entity.AgentConfiguration;
import ua.cn.stu.diploma.system_monitor_java.service.AgentConfigurationService;
import ua.cn.stu.diploma.system_monitor_java.service.AgentStatusScheduler;

@RestController
@RequestMapping("/api/agents/{agentId}/config")
public class AgentConfigController {

    @Autowired
    private AgentConfigurationService configService;
    private AgentStatusScheduler agentStatusScheduler;

    @GetMapping
    public ResponseEntity<AgentConfigurationDto> getConfig(@PathVariable Long  agentId) {
        AgentConfiguration config = configService.getByAgentId(agentId);
        return ResponseEntity.ok(new AgentConfigurationDto(config.getSendIntervalSeconds()));
    }

    @PutMapping
    public ResponseEntity<AgentConfigurationDto> updateConfig(
            @PathVariable Long agentId,
            @RequestBody AgentConfigurationDto dto) {
        AgentConfiguration updated = configService.updateConfigByAgentId(agentId, dto.getSendIntervalSeconds());
        return ResponseEntity.ok(new AgentConfigurationDto(updated.getSendIntervalSeconds()));
    }

}

