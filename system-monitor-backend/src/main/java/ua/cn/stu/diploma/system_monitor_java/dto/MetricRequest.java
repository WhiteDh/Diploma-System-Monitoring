package ua.cn.stu.diploma.system_monitor_java.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MetricRequest {

    @NotNull(message = "agentId is obligated ")
    private Long agentId;

    @NotNull(message = "cpuUsage is obligated")
    @DecimalMin(value = "0.0", inclusive = true, message = "cpuUsage >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "cpuUsage <= 100")
    private Float cpuUsage;

    @NotNull(message = "ramUsage is obligated")
    @DecimalMin(value = "0.0", inclusive = true, message = "ramUsage >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "ramUsage <= 100")
    private Float ramUsage;

    @NotNull(message = "temperature is obligated")
    private Float temperature;

    @NotNull(message = "diskUsage is obligated")
    @DecimalMin(value = "0.0", inclusive = true, message = "diskUsage >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "diskUsage <= 100")
    private Float diskUsage;

    @NotNull(message = "networkIn is obligated")
    @DecimalMin(value = "0.0", inclusive = true, message = "networkIn >= 0")
    private Float networkIn;

    @NotNull(message = "networkOut is obligated")
    @DecimalMin(value = "0.0", inclusive = true, message = "networkOut >= 0")
    private Float networkOut;
}
