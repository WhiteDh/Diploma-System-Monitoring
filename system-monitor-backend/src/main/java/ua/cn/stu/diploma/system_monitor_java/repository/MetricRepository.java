package ua.cn.stu.diploma.system_monitor_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cn.stu.diploma.system_monitor_java.entity.Metric;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findByAgentIdAndCreatedAtBetween(Long agentId,
                                                  LocalDateTime from,
                                                  LocalDateTime to);
    Optional<Metric> findTopByAgentIdOrderByCreatedAtDesc(Long agentId);
}
