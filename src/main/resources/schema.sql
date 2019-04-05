DROP TABLE IF EXISTS tdata;

CREATE TABLE tdata(
  id bigint NOT NULL,
  name varchar(256),
  value numeric(10,5),
  PRIMARY KEY (id)
);