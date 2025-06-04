-- Wstawianie marek
INSERT INTO brand (brand_name) VALUES ('Toyota'); -- ID 1
INSERT INTO brand (brand_name) VALUES ('Ford');   -- ID 2

-- Wstawianie modeli
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1); -- ID 1 (Corolla)
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2);  -- ID 2 (Mustang)

-- Wstawianie kolorów
INSERT INTO color (color) VALUES ('Red');
INSERT INTO color (color) VALUES ('Blue');

-- Wstawianie typów silnika
INSERT INTO engine_type (engine_type) VALUES ('V6');       -- ID 1 (V6)
INSERT INTO engine_type (engine_type) VALUES ('Electric'); -- ID 2 (Electric)
INSERT INTO engine_type (engine_type) VALUES ('2.0L I4');

-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe');
INSERT INTO body_type (body_type) VALUES ('Hatchback');

-- Łączenie modeli z silnikami
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 2);
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 3);


INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (2, 1);