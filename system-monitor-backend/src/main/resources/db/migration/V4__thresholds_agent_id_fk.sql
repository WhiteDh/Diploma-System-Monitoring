ALTER TABLE thresholds DROP FOREIGN KEY thresholds_ibfk_1;

ALTER TABLE thresholds DROP COLUMN user_id;

ALTER TABLE thresholds ADD COLUMN agent_id BIGINT UNIQUE;

ALTER TABLE thresholds
ADD CONSTRAINT fk_thresholds_agent FOREIGN KEY (agent_id) REFERENCES agents(id) ON DELETE CASCADE;
