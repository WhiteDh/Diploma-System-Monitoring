package ua.cn.stu.diploma.system_monitor_java.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "agent_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "send_interval_seconds", nullable = false)
    private int sendIntervalSeconds = 10;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
