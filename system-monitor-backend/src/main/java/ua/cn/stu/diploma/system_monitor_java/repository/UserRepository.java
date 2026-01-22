package ua.cn.stu.diploma.system_monitor_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cn.stu.diploma.system_monitor_java.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
