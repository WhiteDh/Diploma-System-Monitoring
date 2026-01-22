package ua.cn.stu.diploma.system_monitor_java.dto;

import lombok.Data;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class AgentResponse {
    private Long id;
    private String name;
    private OffsetDateTime lastSeen;

    public static AgentResponse fromAgent(Agent agent) {
        AgentResponse response = new AgentResponse();
        response.setId(agent.getId());
        response.setName(agent.getName());
        response.setLastSeen(agent.getLastSeen());
        return response;
    }
}
