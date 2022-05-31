INSERT INTO EVENT (
    TITLE,
    LOCATION,
    START_TIMESTAMP,
    END_TIMESTAMP
) VALUES (
    'Szilveszteri zártkörű rendezvény',
    'Greenwich',
    '2020-01-01 00:00:00',
    '2020-01-01 02:00:00'
),(
    'Májusi mulatság',
    'Budapest',
    '2020-05-01 12:00:00',
    '2020-05-01 16:00:00'
),(
    'Necc party',
    'Debrecen',
    '2020-12-12 00:00:00',
    '2020-12-12 23:59:59'
),(
    'Balaton-átúszás',
    'Révfülöp',
    '2022-07-23 00:00:00',
    '2022-07-23 23:59:59'
);

INSERT INTO SEAT (
    SEAT_ID,
    EVENT_ID,
    PRICE,
    CURRENCY,
    RESERVED,
    VERSION
) VALUES
(1, 1, 1000, 'HUF', 'true', 0),
(2, 1, 1000, 'HUF', 'false', 0),
(3, 1, 1000, 'HUF', 'false', 0),
(4, 1, 1000, 'HUF', 'false', 0),
(5, 1, 1000, 'HUF', 'false', 0),
(6, 1, 1000, 'HUF', 'true', 0),
(7, 1, 1000, 'HUF', 'true', 0),
(8, 1, 1000, 'HUF', 'true', 0),
(9, 1, 1000, 'HUF', 'true', 0),
(10, 1, 1000, 'HUF', 'true', 0),
(11, 2, 2000, 'HUF', 'false', 0),
(12, 2, 2000, 'HUF', 'true', 0),
(13, 2, 2000, 'HUF', 'false', 0),
(14, 2, 2000, 'HUF', 'false', 0),
(15, 2, 2000, 'HUF', 'false', 0),
(16, 2, 2000, 'HUF', 'true', 0),
(17, 2, 2000, 'HUF', 'true', 0),
(18, 2, 2000, 'HUF', 'true', 0),
(19, 2, 2000, 'HUF', 'true', 0),
(20, 2, 2000, 'HUF', 'true', 0),
(21, 3, 3000, 'HUF', 'false', 0),
(22, 3, 3000, 'HUF', 'false', 0),
(23, 3, 3000, 'HUF', 'true', 0),
(24, 3, 3000, 'HUF', 'false', 0),
(25, 3, 3000, 'HUF', 'false', 0),
(26, 3, 3000, 'HUF', 'true', 0),
(27, 3, 3000, 'HUF', 'true', 0),
(28, 3, 3000, 'HUF', 'true', 0),
(29, 3, 3000, 'HUF', 'true', 0),
(30, 3, 3000, 'HUF', 'true', 0),
(31, 4, 1000, 'HUF', 'false', 0),
(32, 4, 30000, 'HUF', 'false', 0);