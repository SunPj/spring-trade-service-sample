CREATE TABLE buy_order (
  id       BIGSERIAL      NOT NULL PRIMARY KEY,
  quantity BIGINT         NOT NULL,
  remain   BIGINT         NOT NULL,
  price    NUMERIC(11, 2) NOT NULL,
  created  TIMESTAMPTZ    NOT NULL,
  version  BIGINT
);

CREATE TABLE sell_order (
  id       BIGSERIAL      NOT NULL PRIMARY KEY,
  quantity BIGINT         NOT NULL,
  remain   BIGINT         NOT NULL,
  price    NUMERIC(11, 2) NOT NULL,
  created  TIMESTAMPTZ    NOT NULL,
  version  BIGINT
);

CREATE TABLE execution (
  id            BIGSERIAL   NOT NULL PRIMARY KEY,
  buy_order_id  BIGINT      NOT NULL,
  sell_order_id BIGINT      NOT NULL,
  quantity      BIGINT      NOT NULL,
  created       TIMESTAMPTZ NOT NULL,
  CONSTRAINT execution_buy_order_id FOREIGN KEY (buy_order_id) REFERENCES buy_order (id),
  CONSTRAINT execution_sell_order_id FOREIGN KEY (sell_order_id) REFERENCES sell_order (id)
);