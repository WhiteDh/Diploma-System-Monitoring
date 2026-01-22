package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.dto.MetricRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.MetricResponse;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.Metric;
import ua.cn.stu.diploma.system_monitor_java.entity.User;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.MetricRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final MetricRepository metricRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    public void saveMetric(String username, MetricRequest req) {
        Agent agent = agentRepository.findById(req.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        if (!agent.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Not your agent");
        }
        Metric m = Metric.builder()
                .agent(agent)
                .cpuUsage(req.getCpuUsage())
                .ramUsage(req.getRamUsage())
                .temperature(req.getTemperature())
                .diskUsage(req.getDiskUsage())
                .networkIn(req.getNetworkIn())
                .networkOut(req.getNetworkOut())
                .createdAt(LocalDateTime.now())
                .build();
        metricRepository.save(m);

    }

    public List<MetricResponse> getHistory(Long agentId, LocalDateTime from, LocalDateTime to, String username) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!agent.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied: agent does not belong to user");
        }

        return metricRepository.findByAgentIdAndCreatedAtBetween(agentId, from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    private MetricResponse toDto(Metric m) {
        MetricResponse r = new MetricResponse();
        r.setId(m.getId());
        r.setAgentId(m.getAgent().getId());
        r.setCpuUsage(m.getCpuUsage());
        r.setRamUsage(m.getRamUsage());
        r.setTemperature(m.getTemperature());
        r.setDiskUsage(m.getDiskUsage());
        r.setNetworkIn(m.getNetworkIn());
        r.setNetworkOut(m.getNetworkOut());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}
