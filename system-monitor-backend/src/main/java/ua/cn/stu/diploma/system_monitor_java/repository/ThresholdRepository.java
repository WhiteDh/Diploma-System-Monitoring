package ua.cn.stu.diploma.system_monitor_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.Threshold;

import java.util.Optional;

public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    Optional<Threshold> findByAgent(Agent agent);
    Optional<Threshold> findByAgentId(Long agentId);}
