CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    demand INT NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    start_time VARCHAR(20),
    end_time VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS vehicles (
    id BIGSERIAL PRIMARY KEY,
    capacity INT NOT NULL,
    shift_start VARCHAR(20),
    shift_end VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS routes (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    distance DOUBLE PRECISION,
    duration VARCHAR(20),
    CONSTRAINT fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS route_stops (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    stop_sequence INT NOT NULL,
    CONSTRAINT fk_route FOREIGN KEY (route_id) REFERENCES routes (id) ON DELETE CASCADE,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
    );
