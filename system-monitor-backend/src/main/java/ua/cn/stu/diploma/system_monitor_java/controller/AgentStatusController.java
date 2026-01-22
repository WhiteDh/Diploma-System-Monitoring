package ua.cn.stu.diploma.system_monitor_java.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class AgentStatusController {

    private final SimpMessagingTemplate messagingTemplate;

    public AgentStatusController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendStatusUpdate(Long agentId, boolean isOnline) {
        Map<String, Object> status = Map.of("agentId", agentId, "online", isOnline);
        messagingTemplate.convertAndSend("/topic/status", status);
    }
}