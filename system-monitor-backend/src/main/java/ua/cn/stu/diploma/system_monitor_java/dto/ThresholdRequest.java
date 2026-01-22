package ua.cn.stu.diploma.system_monitor_java.dto;

import lombok.Data;

@Data
public class ThresholdRequest {
    private Float cpuMax;
    private Float ramMax;
    private Float tempMax;
    private Float diskMax;
    private Float networkInMax;
    private Float networkOutMax;
}
