package ua.cn.stu.diploma.system_monitor_java.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Metric {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "cpu_usage")
    private Float cpuUsage;

    @Column(name = "ram_usage")
    private Float ramUsage;

    private Float temperature;

    @Column(name = "disk_usage")
    private Float diskUsage;

    @Column(name = "network_in")
    private Float networkIn;

    @Column(name = "network_out")
    private Float networkOut;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
