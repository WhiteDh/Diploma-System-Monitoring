package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.Metric;
import ua.cn.stu.diploma.system_monitor_java.entity.Threshold;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.MetricRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.ThresholdRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final AgentRepository agentRepository;
    private final MetricRepository metricRepository;
    private final ThresholdRepository thresholdRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private final Map<Long, Map<String, LocalDateTime>> lastNotificationTimePerAgent = new HashMap<>();

    private static final int NOTIFICATION_INTERVAL_MINUTES = 5;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void checkThresholds() {
        List<Agent> agents = agentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Agent agent : agents) {
            Optional<Metric> optionalMetric = metricRepository.findTopByAgentIdOrderByCreatedAtDesc(agent.getId());
            if (optionalMetric.isEmpty()) continue;
            Metric latestMetric = optionalMetric.get();

            Optional<Threshold> optionalThreshold = thresholdRepository.findByAgentId(agent.getId());
            if (optionalThreshold.isEmpty()) continue;
            Threshold threshold = optionalThreshold.get();

            Map<String, Float> currentMetrics = new HashMap<>();
            if (latestMetric.getCpuUsage() != null) currentMetrics.put("CPU", latestMetric.getCpuUsage());
            if (latestMetric.getRamUsage() != null) currentMetrics.put("RAM", latestMetric.getRamUsage());
            if (latestMetric.getTemperature() != null) currentMetrics.put("Temp", latestMetric.getTemperature());
            if (latestMetric.getDiskUsage() != null) currentMetrics.put("Disk", latestMetric.getDiskUsage());
            if (latestMetric.getNetworkIn() != null) currentMetrics.put("NetIn", latestMetric.getNetworkIn());
            if (latestMetric.getNetworkOut() != null) currentMetrics.put("NetOut", latestMetric.getNetworkOut());

            Map<String, Float> thresholds = new HashMap<>();
            if (threshold.getCpuMax() != null) thresholds.put("CPU", threshold.getCpuMax());
            if (threshold.getRamMax() != null) thresholds.put("RAM", threshold.getRamMax());
            if (threshold.getTempMax() != null) thresholds.put("Temp", threshold.getTempMax());
            if (threshold.getDiskMax() != null) thresholds.put("Disk", threshold.getDiskMax());
            if (threshold.getNetworkInMax() != null) thresholds.put("NetIn", threshold.getNetworkInMax());
            if (threshold.getNetworkOutMax() != null) thresholds.put("NetOut", threshold.getNetworkOutMax());

            Map<String, LocalDateTime> agentNotifications =
                    lastNotificationTimePerAgent.computeIfAbsent(agent.getId(), k -> new HashMap<>());

            Map<String, Float> exceededMetricsAll = new LinkedHashMap<>();
            Map<String, Float> exceededMetricsToNotify = new LinkedHashMap<>();

            for (String metricName : currentMetrics.keySet()) {
                Float currentValue = currentMetrics.get(metricName);
                Float maxThreshold = thresholds.get(metricName);

                if (maxThreshold == null) continue;

                boolean exceeded = isExceeded(currentValue, maxThreshold);
                LocalDateTime lastNotificationTime = agentNotifications.get(metricName);

                if (exceeded) {
                    exceededMetricsAll.put(metricName, currentValue);

                    if (lastNotificationTime == null || lastNotificationTime.plusMinutes(NOTIFICATION_INTERVAL_MINUTES).isBefore(now)) {
                        exceededMetricsToNotify.put(metricName, currentValue);
                        agentNotifications.put(metricName, now);
                    }
                } else {
                    agentNotifications.remove(metricName);
                }
            }

            if (!exceededMetricsToNotify.isEmpty()) {
                sendNotification(agent, exceededMetricsAll);
            }
        }
    }

    private boolean isExceeded(Float currentValue, Float thresholdValue) {
        return currentValue != null && thresholdValue != null && currentValue > thresholdValue;
    }

    private void sendNotification(Agent agent, Map<String, Float> exceededMetrics) {
        if (agent.getUser() == null || agent.getUser().getUsername() == null) {
            return;
        }

        if (agent.getLastSeen() == null || !agent.getLastSeen().isAfter(OffsetDateTime.now().minusMinutes(2))) {
            return;
        }

        String destination = "/topic/alerts/" + agent.getUser().getUsername();

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Agent ").append(agent.getName()).append(": thresholds for metrics exceeded: ");

        exceededMetrics.forEach((metric, value) ->
                messageBuilder.append(String.format("%s (%.2f), ", metric, value))
        );

        String message = messageBuilder.substring(0, messageBuilder.length() - 2);

        Map<String, Object> payload = Map.of(
                "agentId", agent.getId(),
                "agentName", agent.getName(),
                "metrics", exceededMetrics,
                "message", message
        );

        messagingTemplate.convertAndSend(destination, payload);
    }
}
