
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agents (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT,
                        name VARCHAR(100) NOT NULL,
                        last_seen TIMESTAMP NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE metrics (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         agent_id BIGINT,
                         cpu_usage FLOAT,
                         ram_usage FLOAT,
                         temperature FLOAT,
                         disk_usage FLOAT,
                         network_in FLOAT,
                         network_out FLOAT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE
);
CREATE TABLE thresholds (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT,
                            cpu_max FLOAT,
                            ram_max FLOAT,
                            temp_max FLOAT,
                            disk_max FLOAT,
                            network_in_max FLOAT,
                            network_out_max FLOAT,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
