INSERT INTO buy_order (id, price, quantity, remain, version, created)
VALUES (17, 100, 100, 0, 1, now()),
  (18, 600, 1000, 500, 1, now());

INSERT INTO sell_order (id, price, quantity, remain, version, created)
VALUES (111, 50, 150, 50, 1, now()),
  (222, 500, 500, 0, 1, now());

INSERT INTO execution (id, buy_order_id, sell_order_id, quantity, created)
VALUES (11, 17, 111, 100, now()), (22, 18, 222, 500, now());