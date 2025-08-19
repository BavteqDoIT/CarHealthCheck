-- Wstawianie marek (ID: 1 i 2)
INSERT INTO brand (brand_name) VALUES ('Toyota');
INSERT INTO brand (brand_name) VALUES ('Ford');

-- Wstawianie modeli (ID: 1 i 2)
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1);
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2);

-- Wstawianie kolorów (ID: 1 i 2)
INSERT INTO color (color) VALUES ('Red');
INSERT INTO color (color) VALUES ('Blue');

-- Wstawianie typów paliwa (ID: 1, 2 i 3)
INSERT INTO fuel_type (fuel) VALUES ('Petrol');
INSERT INTO fuel_type (fuel) VALUES ('Diesel');
INSERT INTO fuel_type (fuel) VALUES ('Electric');

-- Wstawianie typów silnika z poprawnymi referencjami do paliwa
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('2.0L I4', 1);
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('V6', 1);
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('Electric Motor', 3);


-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe');
INSERT INTO body_type (body_type) VALUES ('Hatchback');

-- Łączenie modeli z silnikami
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 1); -- Corolla (ID 1) → 2.0L I4 (ID 1)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 3); -- Corolla (ID 1) → Electric Motor (ID 3)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (2, 2); -- Mustang (ID 2) → V6 (ID 2)
