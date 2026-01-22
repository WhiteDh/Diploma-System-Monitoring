package ua.cn.stu.diploma.system_monitor_java.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "thresholds")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Threshold {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "cpu_max")
    private Float cpuMax;

    @Column(name = "ram_max")
    private Float ramMax;

    @Column(name = "temp_max")
    private Float tempMax;

    @Column(name = "disk_max")
    private Float diskMax;

    @Column(name = "network_in_max")
    private Float networkInMax;

    @Column(name = "network_out_max")
    private Float networkOutMax;
}
