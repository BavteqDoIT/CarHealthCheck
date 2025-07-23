CREATE TABLE IF NOT EXISTS brand (
                                     id BIGSERIAL PRIMARY KEY,
                                     brand_name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS model_type (
                                          id BIGSERIAL PRIMARY KEY,
                                          model_type VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS color (
                                     id BIGSERIAL PRIMARY KEY,
                                     color VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS engine_type (
                                           id BIGSERIAL PRIMARY KEY,
                                           engine_type VARCHAR(255) NOT NULL
    );

CREATE TABLE model_engine_type (
                                   model_type_id BIGINT NOT NULL,
                                   engine_type_id BIGINT NOT NULL,
                                   PRIMARY KEY (model_type_id, engine_type_id),
                                   FOREIGN KEY (model_type_id) REFERENCES model_type(id) ON DELETE CASCADE,
                                   FOREIGN KEY (engine_type_id) REFERENCES engine_type(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS body_type (
                                         id BIGSERIAL PRIMARY KEY,
                                         body_type VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS car (
                                   id BIGSERIAL PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    model_type_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    engine_type_id BIGINT NOT NULL,
    body_type_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE,
    FOREIGN KEY (model_type_id) REFERENCES model_type(id) ON DELETE CASCADE,
    FOREIGN KEY (color_id) REFERENCES color(id) ON DELETE CASCADE,
    FOREIGN KEY (engine_type_id) REFERENCES engine_type(id) ON DELETE CASCADE,
    FOREIGN KEY (body_type_id) REFERENCES body_type(id) ON DELETE CASCADE
    );