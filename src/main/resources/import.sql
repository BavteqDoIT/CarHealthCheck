-- Wstawianie marek
INSERT INTO brand (brand_name) VALUES ('Toyota');
INSERT INTO brand (brand_name) VALUES ('Ford');

-- Wstawianie modeli (z poprawionym brand_id)
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1); -- Toyota
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2);  -- Ford

-- Wstawianie kolorów
INSERT INTO color (color) VALUES ('Red');
INSERT INTO color (color) VALUES ('Blue');

-- Wstawianie typów silnika
INSERT INTO engine_type (engine_type) VALUES ('V6');
INSERT INTO engine_type (engine_type) VALUES ('Electric');

-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe');
INSERT INTO body_type (body_type) VALUES ('Hatchback');
