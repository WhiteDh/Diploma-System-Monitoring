package ua.cn.stu.diploma.system_monitor_java.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
}
