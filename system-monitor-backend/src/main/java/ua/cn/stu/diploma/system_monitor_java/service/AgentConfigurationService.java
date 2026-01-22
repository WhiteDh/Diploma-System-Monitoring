package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.AgentConfiguration;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentConfigurationRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;

@Service
@RequiredArgsConstructor
public class AgentConfigurationService {

    private final AgentConfigurationRepository agentConfigurationRepository;
    private final AgentRepository agentRepository;

    public AgentConfiguration getByAgentId(Long agentId) {
        return agentConfigurationRepository.findByAgentId(agentId)
                .orElseGet(() -> {
                    Agent agent = new Agent();
                    agent.setId(agentId);
                    return agentConfigurationRepository.save(AgentConfiguration.builder()
                            .agent(agent)
                            .sendIntervalSeconds(10)
                            .build());
                });
    }

    public AgentConfiguration updateConfigByAgentId(Long agentId, int sendIntervalSeconds) {
        AgentConfiguration config = getByAgentId(agentId);
        config.setSendIntervalSeconds(sendIntervalSeconds);
        return agentConfigurationRepository.save(config);
    }

    public AgentConfiguration getByAgentUuid(String uuid) {
        Agent agent = agentRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Agent not found by UUID"));
        return agentConfigurationRepository.findByAgent(agent)
                .orElseGet(() -> createDefaultConfig(agent));
    }

    public AgentConfiguration updateConfigByUuid(String uuid, int sendIntervalSeconds) {
        AgentConfiguration config = getByAgentUuid(uuid);
        config.setSendIntervalSeconds(sendIntervalSeconds);
        return agentConfigurationRepository.save(config);
    }

    private AgentConfiguration createDefaultConfig(Agent agent) {
        return agentConfigurationRepository.save(AgentConfiguration.builder()
                .agent(agent)
                .sendIntervalSeconds(10)
                .build());
    }
}
