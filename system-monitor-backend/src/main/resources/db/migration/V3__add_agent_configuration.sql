CREATE TABLE agent_configurations (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      agent_id BIGINT NOT NULL,
                                      send_interval_seconds INT NOT NULL DEFAULT 10,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE
);
