CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS public.brand (
    id BIGSERIAL PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.model_type (
    id BIGSERIAL PRIMARY KEY,
    model_type VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.color (
    id BIGSERIAL PRIMARY KEY,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.fuel_type (
    id BIGSERIAL PRIMARY KEY,
    fuel VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS public.engine_type (
    id BIGSERIAL PRIMARY KEY,
    engine_type VARCHAR(255) NOT NULL,
    fuel_type_id BIGINT NOT NULL,
    FOREIGN KEY (fuel_type_id) REFERENCES fuel_type(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.model_engine_type (
    model_type_id BIGINT NOT NULL,
    engine_type_id BIGINT NOT NULL,
    PRIMARY KEY (model_type_id, engine_type_id),
    FOREIGN KEY (model_type_id) REFERENCES model_type(id) ON DELETE CASCADE,
   FOREIGN KEY (engine_type_id) REFERENCES engine_type(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.body_type (
    id BIGSERIAL PRIMARY KEY,
    body_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.drive_type (
    id BIGSERIAL PRIMARY KEY,
    drive_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.gearbox_type (
     id BIGSERIAL PRIMARY KEY,
     gearbox_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.car (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    production_year INT NOT NULL,
    mileage INT NOT NULL,
    brand_id BIGINT NOT NULL,
    model_type_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    engine_type_id BIGINT NOT NULL,
    body_type_id BIGINT NOT NULL,
    drive_type_id BIGINT NOT NULL,
    gearbox_type_id BIGINT NOT NULL,
    vin VARCHAR(17) NOT NULL,
    first_registration_date DATE,
    foreign_registered BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE,
    FOREIGN KEY (model_type_id) REFERENCES model_type(id) ON DELETE CASCADE,
    FOREIGN KEY (color_id) REFERENCES color(id) ON DELETE CASCADE,
    FOREIGN KEY (engine_type_id) REFERENCES engine_type(id) ON DELETE CASCADE,
    FOREIGN KEY (body_type_id) REFERENCES body_type(id) ON DELETE CASCADE,
    FOREIGN KEY (drive_type_id) REFERENCES drive_type(id) ON DELETE CASCADE,
    FOREIGN KEY (gearbox_type_id) REFERENCES gearbox_type(id) ON DELETE CASCADE
);