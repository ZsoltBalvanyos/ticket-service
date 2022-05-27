INSERT INTO CUSTOMER (USER_ID, NAME, EMAIL) VALUES
(1000, 'Teszt Aladár', 'teszt.aladar@otpmobil.com'),
(2000, 'Teszt Benedek', 'teszt.benedek@otpmobil.com'),
(3000, 'Teszt Cecília', 'teszt.cecilia@otpmobil.com');

INSERT INTO USER_DEVICE (USER_ID, DEVICE_HASH) VALUES
(1000, 'A'),
(1000, 'B'),
(1000, 'C'),
(2000, 'D'),
(3000, 'E');

INSERT INTO USER_TOKEN (USER_ID, TOKEN) VALUES
(1000, 'A'),
(1000, 'B'),
(1000, 'C'),
(2000, 'D'),
(3000, 'E');

INSERT INTO USER_BANK_CARD (
    CARD_ID,
    USER_ID,
    CARD_NUMBER,
    CVC,
    NAME,
    AMOUNT,
    CURRENCY
) VALUES
('C0001', 1000, 5299706965433676, 123, 'Teszt Aladár', 1000, 'HUF'),
('C0002', 2000, 5390508354245119, 456, 'Teszt Benedek', 2000, 'HUF'),
('C0003', 3000, 4929088924014470, 789, 'Teszt Cecília', 3000, 'HUF');

INSERT INTO PARTNER_BALANCE (PARTNER_ID, AMOUNT) VALUES
(1, 80000),
(2, 50000);