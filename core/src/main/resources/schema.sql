DROP TABLE IF EXISTS RESERVED_AMOUNT;
DROP TABLE IF EXISTS PARTNER_BALANCE;
DROP TABLE IF EXISTS USER_BANK_CARD;
DROP TABLE IF EXISTS USER_TOKEN;
DROP TABLE IF EXISTS USER_DEVICE;
DROP TABLE IF EXISTS CUSTOMER;

CREATE TABLE CUSTOMER (
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
            REFERENCES CUSTOMER(USER_ID)
            ON DELETE CASCADE
);

CREATE TABLE USER_TOKEN (
    USER_ID BIGINT NOT NULL,
    TOKEN TEXT NOT NULL,

    PRIMARY KEY (USER_ID, TOKEN),

    CONSTRAINT FK_USER_USER_TOKEN
        FOREIGN KEY (USER_ID)
            REFERENCES CUSTOMER(USER_ID)
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
            REFERENCES CUSTOMER(USER_ID)
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