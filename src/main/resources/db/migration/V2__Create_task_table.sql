CREATE SEQUENCE t_task_seq AS BIGINT START WITH 10 INCREMENT BY 10;

GRANT USAGE, SELECT ON SEQUENCE t_task_seq TO ${springDatasourceUsername};

CREATE TABLE t_task (
  id BIGINT NOT NULL DEFAULT nextval('t_task_seq'),
  entity_id  BIGINT,
  next_retry_time TIMESTAMP,
  number_of_retries INT,
  created_at TIMESTAMP,
  sent BOOL,
  CONSTRAINT pk_t_task PRIMARY KEY (id)
);

CREATE INDEX ON t_task(next_retry_time, number_of_retries) where sent = false;
CREATE INDEX ON t_task(entity_id, created_at) where sent = false;

GRANT SELECT, INSERT, UPDATE ON t_task TO ${springDatasourceUsername};