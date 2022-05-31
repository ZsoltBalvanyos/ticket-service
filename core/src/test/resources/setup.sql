DROP TABLE IF EXISTS RESERVED_AMOUNT;
DROP TABLE IF EXISTS PARTNER_BALANCE;
DROP TABLE IF EXISTS USER_BANK_CARD;
DROP TABLE IF EXISTS USER_TOKEN;
DROP TABLE IF EXISTS USER_DEVICE;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
                       USER_ID BIGSERIAL PRIMARY KEY,
                       NAME TEXT NOT NULL,
                       EMAIL TEXT NOT NULL
);

CREATE TABLE USER_DEVICE (
                             USER_ID BIGINT NOT NULL,
                             DEVICE_HASH TEXT NOT NULL,

                             PRIMARY KEY (USER_ID, DEVICE_HASH),

                             CONSTRAINT FK_USER_USER_DEVICE
                                 FOREIGN KEY (USER_ID)
                                     REFERENCES USERS(USER_ID)
                                     ON DELETE CASCADE
);

CREATE TABLE USER_TOKEN (
                            USER_ID BIGINT NOT NULL,
                            TOKEN TEXT NOT NULL,

                            PRIMARY KEY (USER_ID, TOKEN),

                            CONSTRAINT FK_USER_USER_TOKEN
                                FOREIGN KEY (USER_ID)
                                    REFERENCES USERS(USER_ID)
                                    ON DELETE CASCADE
);

CREATE TABLE USER_BANK_CARD (
                                CARD_ID TEXT PRIMARY KEY,
                                USER_ID BIGINT NOT NULL,
                                CARD_NUMBER BIGINT NOT NULL,
                                CVC SMALLINT NOT NULL,
                                NAME TEXT NOT NULL,
                                AMOUNT DECIMAL NOT NULL,
                                CURRENCY CHAR(3) NOT NULL,

                                CONSTRAINT FK_USER_USER_BANK_CARD
                                    FOREIGN KEY (USER_ID)
                                        REFERENCES USERS(USER_ID)
                                        ON DELETE CASCADE
);

CREATE TABLE PARTNER_BALANCE(
                                PARTNER_ID BIGINT PRIMARY KEY,
                                AMOUNT DECIMAL NOT NULL
);

CREATE TABLE RESERVED_AMOUNT (
                                 TRANSACTION_ID BIGINT PRIMARY KEY,
                                 CARD_ID TEXT NOT NULL,
                                 AMOUNT DECIMAL NOT NULL,
                                 RESERVED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT FK_CARD_ID_RESERVED_AMOUNT
                                     FOREIGN KEY (CARD_ID)
                                         REFERENCES USER_BANK_CARD(CARD_ID)
                                         ON DELETE CASCADE
);


INSERT INTO USERS (USER_ID, NAME, EMAIL) VALUES
(1000, 'Teszt Aladár', 'teszt.aladar@otpmobil.com'),
(2000, 'Teszt Benedek', 'teszt.benedek@otpmobil.com'),
(3000, 'Teszt Cecília', 'teszt.cecilia@otpmobil.com');

INSERT INTO USER_DEVICE (USER_ID, DEVICE_HASH) VALUES
(1000, 'F67C2BCBFCFA30FCCB36F72DCA22A817'),
(1000, '0F1674BD19D3BBDD4C39E14734FFB876'),
(1000, '3AE5E9658FBD7D4048BD40820B7D227D'),
(2000, 'FADDFEA562F3C914DCC81956682DB0FC'),
(3000, 'E68560872BDB2DF2FFE7ADC091755378');

INSERT INTO USER_TOKEN (USER_ID, TOKEN) VALUES
(1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJkY2N0MyQkNCRkNGQTMwRkNDQjM2RjcyRENBMjJBODE3'),
(2000, 'dGVzenQuYmVuZWRla0BvdHBtb2JpbC5jb20mMjAwMCZGQURERkVBNTYyRjNDOTE0RENDODE5NTY2ODJEQjBGQw=='),
(3000, 'dGVzenQuY2VjaWxpYUBvdHBtb2JpbC5jb20mMzAwMCZFNjg1NjA4NzJCREIyREYyRkZFN0FEQzA5MTc1NTM3OA=='),
(1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJjBGMTY3NEJEMTlEM0JCREQ0QzM5RTE0NzM0RkZCODc2'),
(1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJjNBRTVFOTY1OEZCRDdENDA0OEJENDA4MjBCN0QyMjdE');

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
