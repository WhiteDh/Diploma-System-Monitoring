package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.controller.AgentStatusController;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentStatusScheduler {

    private final AgentRepository agentRepository;
    private final AgentStatusController agentStatusController;

    @Scheduled(fixedDelay = 10000)
    public void checkAgentStatuses() {
        List<Agent> agents = agentRepository.findAll();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        for (Agent agent : agents) {
            boolean isOnline = agent.getLastSeen().isAfter(now.minusMinutes(2));
            agentStatusController.sendStatusUpdate(agent.getId(), isOnline);
        }
    }
}
