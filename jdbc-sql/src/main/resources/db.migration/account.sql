CREATE TABLE IF NOT EXISTS account (
  id            BIGINT NOT NULL,
  fist_name     VARCHAR(255) NOT NULL,
  last_name     VARCHAR(255) NOT NULL,
  email         VARCHAR(255) NOT NULL,
  birthday      TIMESTAMP NOT NULL,
  balance       DECIMAL(19, 4),
  creation_time TIMESTAMP NOT NULL DEFAULT now(),

  CONSTRAINT account_pk PRIMARY KEY (id),
  CONSTRAINT account_email_uq UNIQUE (email)
);

