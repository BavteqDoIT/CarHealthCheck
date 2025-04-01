CREATE TABLE IF NOT EXISTS brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS model_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    model_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS color (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS engine_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    engine_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS body_type (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    body_type VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS car (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand_id BIGINT NOT NULL,
    model_type_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    engine_type_id BIGINT NOT NULL,
    body_type_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id),
    FOREIGN KEY (model_type_id) REFERENCES model_type(id),
    FOREIGN KEY (color_id) REFERENCES color(id),
    FOREIGN KEY (engine_type_id) REFERENCES engine_type(id),
    FOREIGN KEY (body_type_id) REFERENCES body_type(id)
);
