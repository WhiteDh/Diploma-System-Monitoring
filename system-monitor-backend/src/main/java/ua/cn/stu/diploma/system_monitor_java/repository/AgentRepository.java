package ua.cn.stu.diploma.system_monitor_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.User;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByUuidAndUser(String uuid, User user);
    Optional<Agent> findByUuid(String uuid);
    List<Agent> findByUser(User user);
    Optional<Agent> findByIdAndUserUsername(Long id, String username);
}
