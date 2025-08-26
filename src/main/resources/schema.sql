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
    vin VARCHAR(17) NOT NULL UNIQUE,
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
CREATE TABLE IF NOT EXISTS public.paint_check (
    id BIGSERIAL PRIMARY KEY,
    car_id BIGINT NOT NULL UNIQUE,

    hood_different BOOLEAN DEFAULT FALSE,
    roof_different BOOLEAN DEFAULT FALSE,
    front_left_door_different BOOLEAN DEFAULT FALSE,
    rear_left_door_different BOOLEAN DEFAULT FALSE,
    front_right_door_different BOOLEAN DEFAULT FALSE,
    rear_right_door_different BOOLEAN DEFAULT FALSE,
    trunk_different BOOLEAN DEFAULT FALSE,
    front_bumper_different BOOLEAN DEFAULT FALSE,
    rear_bumper_different BOOLEAN DEFAULT FALSE,

    a_pillar_left_different BOOLEAN DEFAULT FALSE,
    a_pillar_right_different BOOLEAN DEFAULT FALSE,
    b_pillar_left_different BOOLEAN DEFAULT FALSE,
    b_pillar_right_different BOOLEAN DEFAULT FALSE,
    c_pillar_left_different BOOLEAN DEFAULT FALSE,
    c_pillar_right_different BOOLEAN DEFAULT FALSE,

    front_left_wheel_arch_different BOOLEAN DEFAULT FALSE,
    front_right_wheel_arch_different BOOLEAN DEFAULT FALSE,
    rear_left_wheel_arch_different BOOLEAN DEFAULT FALSE,
    rear_right_wheel_arch_different BOOLEAN DEFAULT FALSE,

    hood_thickness INT,
    roof_thickness INT,
    front_bumper INT,
    rear_bumper INT,
    front_left_door_thickness INT,
    rear_left_door_thickness INT,
    front_right_door_thickness INT,
    rear_right_door_thickness INT,
    trunk_thickness INT,
    a_pillar_left_thickness INT,
    a_pillar_right_thickness INT,
    b_pillar_left_thickness INT,
    b_pillar_right_thickness INT,
    c_pillar_left_thickness INT,
    c_pillar_right_thickness INT,
    front_left_wheel_arch_thickness INT,
    front_right_wheel_arch_thickness INT,
    rear_left_wheel_arch_thickness INT,
    rear_right_wheel_arch_thickness INT,

    FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.paint_damage (
     id BIGSERIAL PRIMARY KEY,
     paint_id BIGINT NOT NULL,
     body_part VARCHAR(50),
     damage_type VARCHAR(50),
     size VARCHAR(50),
     FOREIGN KEY (paint_id) REFERENCES paint_check(id) ON DELETE CASCADE
);