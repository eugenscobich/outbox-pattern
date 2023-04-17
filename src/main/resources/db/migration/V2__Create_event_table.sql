CREATE SEQUENCE t_event_seq AS BIGINT START WITH 10 INCREMENT BY 10;

GRANT USAGE, SELECT ON SEQUENCE t_event_seq TO ${springDatasourceUsername};

CREATE TABLE t_event (
  id BIGINT NOT NULL DEFAULT nextval('t_event_seq'),
  name  VARCHAR(50),
  CONSTRAINT pk_t_event PRIMARY KEY (id)
);