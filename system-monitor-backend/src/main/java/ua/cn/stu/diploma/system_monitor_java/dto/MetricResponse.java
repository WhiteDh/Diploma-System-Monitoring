package ua.cn.stu.diploma.system_monitor_java.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MetricResponse {
    private Long id;
    private Long agentId;
    private Float cpuUsage;
    private Float ramUsage;
    private Float temperature;
    private Float diskUsage;
    private Float networkIn;
    private Float networkOut;
    private LocalDateTime createdAt;
}
