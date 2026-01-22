package ua.cn.stu.diploma.system_monitor_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class SystemMonitorJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemMonitorJavaApplication.class, args);
	}

}
