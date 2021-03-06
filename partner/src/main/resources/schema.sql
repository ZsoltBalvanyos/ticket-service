DROP TABLE IF EXISTS SEAT;
DROP TABLE IF EXISTS EVENT;

CREATE TABLE EVENT (
    EVENT_ID BIGSERIAL PRIMARY KEY ,
    TITLE TEXT NOT NULL,
    LOCATION TEXT NOT NULL,
    START_TIMESTAMP TIMESTAMP NOT NULL,
    END_TIMESTAMP TIMESTAMP NOT NULL
);

CREATE TABLE SEAT (
    RESERVATION_ID BIGSERIAL PRIMARY KEY,
    EVENT_ID BIGINT NOT NULL,
    SEAT_ID BIGINT NOT NULL,
    PRICE NUMERIC NOT NULL,
    CURRENCY CHAR(3) NOT NULL,
    RESERVED TEXT NOT NULL DEFAULT 'false' CHECK (RESERVED IN ('true', 'false')),
    VERSION INTEGER NOT NULL,

    UNIQUE (EVENT_ID, SEAT_ID),

    CONSTRAINT FK_EVENT_ID_RESERVATION
        FOREIGN KEY (EVENT_ID)
            REFERENCES EVENT(EVENT_ID)
            ON DELETE CASCADE
)