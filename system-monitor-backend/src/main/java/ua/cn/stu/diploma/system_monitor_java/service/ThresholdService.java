package ua.cn.stu.diploma.system_monitor_java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.cn.stu.diploma.system_monitor_java.dto.ThresholdRequest;
import ua.cn.stu.diploma.system_monitor_java.dto.ThresholdResponse;
import ua.cn.stu.diploma.system_monitor_java.entity.Agent;
import ua.cn.stu.diploma.system_monitor_java.entity.Threshold;
import ua.cn.stu.diploma.system_monitor_java.entity.User;
import ua.cn.stu.diploma.system_monitor_java.repository.AgentRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.ThresholdRepository;
import ua.cn.stu.diploma.system_monitor_java.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    public ThresholdResponse getThreshold(String username, Long agentId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Agent agent = agentRepository.findById(agentId)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Agent not found or does not belong to user"));

        Threshold t = thresholdRepository.findByAgentId(agent.getId())
                .orElseGet(() -> {
                    Threshold defaultThreshold = new Threshold();
                    defaultThreshold.setAgent(agent);
                    defaultThreshold.setCpuMax(100f);
                    defaultThreshold.setRamMax(100f);
                    defaultThreshold.setTempMax(100f);
                    defaultThreshold.setDiskMax(100f);
                    defaultThreshold.setNetworkInMax(1000f);
                    defaultThreshold.setNetworkOutMax(1000f);
                    return thresholdRepository.save(defaultThreshold);
                });
        return toDto(t);
    }

    public ThresholdResponse updateThreshold(String username, Long agentId, ThresholdRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Agent agent = agentRepository.findById(agentId)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Agent not found or does not belong to user"));

        Threshold t = thresholdRepository.findByAgentId(agent.getId())
                .orElseGet(() -> {
                    Threshold newT = new Threshold();
                    newT.setAgent(agent);
                    return newT;
                });
        t.setCpuMax(req.getCpuMax());
        t.setRamMax(req.getRamMax());
        t.setTempMax(req.getTempMax());
        t.setDiskMax(req.getDiskMax());
        t.setNetworkInMax(req.getNetworkInMax());
        t.setNetworkOutMax(req.getNetworkOutMax());
        t = thresholdRepository.save(t);
        return toDto(t);
    }

    private ThresholdResponse toDto(Threshold t) {
        ThresholdResponse r = new ThresholdResponse();
        r.setId(t.getId());
        r.setCpuMax(t.getCpuMax());
        r.setRamMax(t.getRamMax());
        r.setTempMax(t.getTempMax());
        r.setDiskMax(t.getDiskMax());
        r.setNetworkInMax(t.getNetworkInMax());
        r.setNetworkOutMax(t.getNetworkOutMax());
        return r;
    }
}

