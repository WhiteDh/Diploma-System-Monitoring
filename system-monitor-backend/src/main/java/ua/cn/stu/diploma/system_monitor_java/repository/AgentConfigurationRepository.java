package ua.cn.stu.diploma.system_monitor_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.AgentConfiguration;

import java.util.Optional;

public interface AgentConfigurationRepository extends JpaRepository<AgentConfiguration, Long> {
    Optional<AgentConfiguration> findByAgentId(Long agentId);

    Optional<AgentConfiguration> findByAgent(Agent agent);
}
