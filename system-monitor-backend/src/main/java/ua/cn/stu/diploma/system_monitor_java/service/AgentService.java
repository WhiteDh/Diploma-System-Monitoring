package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.dto.AgentRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.AgentResponse;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.User;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;
    private final UserRepository userRepository;



    public AgentResponse registerAgent(String username, AgentRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        OffsetDateTime utcNow = OffsetDateTime.now(ZoneOffset.UTC);

        Agent agent = agentRepository.findByUuidAndUser(req.getUuid(), user)
                .orElseGet(() -> {
                    Agent newAgent = Agent.builder()
                            .uuid(req.getUuid())
                            .name(req.getName())
                            .user(user)
                            .lastSeen(utcNow)
                            .build();
                    return agentRepository.save(newAgent);
                });

        agent.setLastSeen(utcNow);
        agentRepository.save(agent);

        return AgentResponse.fromAgent(agent);
    }

    public List<AgentResponse> listAgents(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return agentRepository.findByUser(user)
                .stream()
                .map(AgentResponse::fromAgent)
                .toList();
    }

    public void updateLastSeen(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setLastSeen(OffsetDateTime.now(ZoneOffset.UTC));



        agentRepository.save(agent);
    }

    public AgentResponse getAgentByIdForUser(Long agentId, String username) {
        Agent agent = agentRepository.findByIdAndUserUsername(agentId, username)
                .orElseThrow(() -> new AccessDeniedException("Access denied or agent not found"));

        return AgentResponse.fromAgent(agent);
    }
}
