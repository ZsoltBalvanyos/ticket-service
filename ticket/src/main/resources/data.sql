INSERT INTO PARTNER (PARTNER_ID, NAME, URL) VALUES
(1, 'SzuperJegyek', 'localhost:9999'),
(2, 'BestTickets', 'localhost:9994');

INSERT INTO PROMOTED_EVENT(EVENT_ID, PARTNER_ID) VALUES
(1, 2),
(2, 2),
(3, 2);