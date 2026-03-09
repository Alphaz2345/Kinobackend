

CREATE DATABASE IF NOT EXISTS kinodb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kinodb;

-- ----------------------------------------------------------------
-- 1. ROLES (Roller — skal oprettes FØR employees)
-- ----------------------------------------------------------------
CREATE TABLE roles (
                       id      INT AUTO_INCREMENT PRIMARY KEY,
                       name    VARCHAR(100) NOT NULL UNIQUE
    -- 'admin', 'reservation_staff', 'operator', 'ticket_inspector'
);

-- ----------------------------------------------------------------
-- 2. CATEGORIES (Filmkategorier)
-- ----------------------------------------------------------------
CREATE TABLE categories (
                            id      INT AUTO_INCREMENT PRIMARY KEY,
                            name    VARCHAR(100) NOT NULL UNIQUE
);

-- ----------------------------------------------------------------
-- 3. CUSTOMERS (Kunder)
-- ----------------------------------------------------------------
CREATE TABLE customers (
                           id              INT AUTO_INCREMENT PRIMARY KEY,
                           full_name       VARCHAR(255)    NOT NULL,
                           phone           VARCHAR(20),
                           email           VARCHAR(255)    UNIQUE,
                           password_hash   VARCHAR(255),
                           created_at      DATETIME        NOT NULL DEFAULT NOW()
);

-- ----------------------------------------------------------------
-- 4. EMPLOYEES (Medarbejdere)
-- ----------------------------------------------------------------
CREATE TABLE employees (
                           id              INT AUTO_INCREMENT PRIMARY KEY,
                           full_name       VARCHAR(255)    NOT NULL,
                           email           VARCHAR(255)    NOT NULL UNIQUE,
                           password_hash   VARCHAR(255)    NOT NULL,
                           role_id         INT             NOT NULL,
                           is_active       TINYINT(1)      NOT NULL DEFAULT 1,
                           created_at      DATETIME        NOT NULL DEFAULT NOW(),
                           FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ----------------------------------------------------------------
-- 5. THEATRES (Biografsale)
-- ----------------------------------------------------------------
CREATE TABLE theatres (
                          id              INT AUTO_INCREMENT PRIMARY KEY,
                          name            VARCHAR(100)    NOT NULL,
                          `rows`          INT             NOT NULL,
                          seats_per_row   INT             NOT NULL,
                          is_blocked      TINYINT(1)      NOT NULL DEFAULT 0,
                          blocked_reason  VARCHAR(255),
                          blocked_until   DATETIME
);

-- ----------------------------------------------------------------
-- 6. SEATS (Sæder)
-- ----------------------------------------------------------------
CREATE TABLE seats (
                       id          INT AUTO_INCREMENT PRIMARY KEY,
                       theatre_id  INT     NOT NULL,
                       row_number  INT     NOT NULL,
                       seat_number INT     NOT NULL,
                       UNIQUE KEY uq_seat (theatre_id, row_number, seat_number),
                       FOREIGN KEY (theatre_id) REFERENCES theatres(id) ON DELETE CASCADE
);

-- ----------------------------------------------------------------
-- 7. MOVIES (Film)
-- ----------------------------------------------------------------
CREATE TABLE movies (
                        id              INT AUTO_INCREMENT PRIMARY KEY,
                        title           VARCHAR(255)    NOT NULL,
                        description     TEXT,
                        duration_min    INT             NOT NULL,
                        age_limit       INT             NOT NULL DEFAULT 0,
                        category_id     INT,
                        poster_url      VARCHAR(500),
                        is_archived     TINYINT(1)      NOT NULL DEFAULT 0,
                        created_at      DATETIME        NOT NULL DEFAULT NOW(),
                        FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- ----------------------------------------------------------------
-- 8. SHOWINGS (Forestillinger)
-- ----------------------------------------------------------------
CREATE TABLE showings (
                          id              INT AUTO_INCREMENT PRIMARY KEY,
                          movie_id        INT             NOT NULL,
                          theatre_id      INT             NOT NULL,
                          start_time      DATETIME        NOT NULL,
                          end_time        DATETIME        NOT NULL,
                          is_extra        TINYINT(1)      NOT NULL DEFAULT 0,
                          status          VARCHAR(50)     NOT NULL DEFAULT 'scheduled',
                          cancelled_at    DATETIME,
                          INDEX idx_theatre_time (theatre_id, start_time, end_time),
                          FOREIGN KEY (movie_id)   REFERENCES movies(id),
                          FOREIGN KEY (theatre_id) REFERENCES theatres(id)
);

-- ----------------------------------------------------------------
-- 9. RESERVATIONS (Reservationer)
-- ----------------------------------------------------------------
CREATE TABLE reservations (
                              id                  INT AUTO_INCREMENT PRIMARY KEY,
                              showing_id          INT             NOT NULL,
                              customer_id         INT,
                              customer_name       VARCHAR(255),
                              customer_phone      VARCHAR(20),
                              reservation_code    VARCHAR(20)     NOT NULL UNIQUE,
                              status              VARCHAR(50)     NOT NULL DEFAULT 'pending',
                              expires_at          DATETIME,
                              created_at          DATETIME        NOT NULL DEFAULT NOW(),
                              updated_at          DATETIME        NOT NULL DEFAULT NOW(),
                              FOREIGN KEY (showing_id)  REFERENCES showings(id),
                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- ----------------------------------------------------------------
-- 10. RESERVATION SEATS
-- ----------------------------------------------------------------
CREATE TABLE reservation_seats (
                                   id              INT AUTO_INCREMENT PRIMARY KEY,
                                   reservation_id  INT     NOT NULL,
                                   seat_id         INT     NOT NULL,
                                   UNIQUE KEY uq_res_seat (reservation_id, seat_id),
                                   FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE,
                                   FOREIGN KEY (seat_id)        REFERENCES seats(id)
);

-- ----------------------------------------------------------------
-- 11. TICKETS (Billetter)
-- ----------------------------------------------------------------
CREATE TABLE tickets (
                         id              INT AUTO_INCREMENT PRIMARY KEY,
                         reservation_id  INT,
                         showing_id      INT             NOT NULL,
                         seat_id         INT             NOT NULL,
                         customer_id     INT,
                         ticket_code     VARCHAR(50)     NOT NULL UNIQUE,
                         price           DECIMAL(8,2)    NOT NULL,
                         is_used         TINYINT(1)      NOT NULL DEFAULT 0,
                         used_at         DATETIME,
                         purchased_at    DATETIME        NOT NULL DEFAULT NOW(),
                         UNIQUE KEY uq_showing_seat (showing_id, seat_id),
                         FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                         FOREIGN KEY (showing_id)     REFERENCES showings(id),
                         FOREIGN KEY (seat_id)        REFERENCES seats(id),
                         FOREIGN KEY (customer_id)    REFERENCES customers(id)
);

-- ----------------------------------------------------------------
-- 12. RESERVATION LOGS (Auditlog)
-- ----------------------------------------------------------------
CREATE TABLE reservation_logs (
                                  id              INT AUTO_INCREMENT PRIMARY KEY,
                                  reservation_id  INT             NOT NULL,
                                  action          VARCHAR(100)    NOT NULL,
                                  performed_by    INT,
                                  note            TEXT,
                                  logged_at       DATETIME        NOT NULL DEFAULT NOW(),
                                  FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                                  FOREIGN KEY (performed_by)   REFERENCES employees(id)
);

-- ================================================================
-- VIEWS (MySQL-syntax — ingen :: cast, ingen DATE_TRUNC)
-- ================================================================

-- Omsætning per dag
CREATE OR REPLACE VIEW v_revenue_per_period AS
SELECT
    DATE(t.purchased_at)    AS period,
    COUNT(t.id)             AS tickets_sold,
    SUM(t.price)            AS total_revenue
FROM tickets t
GROUP BY DATE(t.purchased_at)
ORDER BY period;

-- Belægning per forestilling
CREATE OR REPLACE VIEW v_occupancy_per_showing AS
SELECT
    s.id                                            AS showing_id,
    m.title                                         AS movie_title,
    th.name                                         AS theatre_name,
    s.start_time,
    COUNT(t.id)                                     AS seats_sold,
    (th.`rows` * th.seats_per_row)                  AS total_seats,
    ROUND(
            COUNT(t.id) /
            NULLIF((th.`rows` * th.seats_per_row), 0) * 100
        , 1)                                            AS occupancy_pct
FROM showings s
         JOIN movies   m  ON m.id  = s.movie_id
         JOIN theatres th ON th.id = s.theatre_id
         LEFT JOIN tickets t ON t.showing_id = s.id
GROUP BY s.id, m.title, th.name, s.start_time, th.`rows`, th.seats_per_row;

-- Dårligt performende film
CREATE OR REPLACE VIEW v_poorly_performing_movies AS
SELECT
    m.id,
    m.title,
    COUNT(DISTINCT s.id)            AS total_showings,
    COALESCE(SUM(t.price), 0)       AS total_revenue,
    AVG(occ.occupancy_pct)          AS avg_occupancy
FROM movies m
         LEFT JOIN showings s             ON s.movie_id   = m.id
         LEFT JOIN tickets t              ON t.showing_id = s.id
         LEFT JOIN v_occupancy_per_showing occ ON occ.showing_id = s.id
WHERE m.is_archived = 0
GROUP BY m.id, m.title
ORDER BY avg_occupancy ASC;

-- ================================================================
-- SEED DATA
-- ================================================================
INSERT INTO roles(name) VALUES
                            ('admin'),
                            ('reservation_staff'),
                            ('operator'),
                            ('ticket_inspector');

INSERT INTO categories(name) VALUES
                                 ('Horror'), ('Romance'), ('Action'),
                                 ('Science Fiction'), ('Comedy'), ('Drama'), ('Thriller'), ('Animation');

-- Eksempel-sale (sæder genereres af TheatreService i Java)
INSERT INTO theatres(name, `rows`, seats_per_row) VALUES
                                                      ('Sal 1 (lille)', 20, 12),
                                                      ('Sal 2 (stor)',  25, 16);

-- Test-admin (kodeord: admin123 — skift i produktion!)
INSERT INTO employees(full_name, email, password_hash, role_id) VALUES
    ('Admin Bruger', 'admin@kino.dk', 'admin123', 1);