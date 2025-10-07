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
INSERT INTO engine_type (engine_type, capacity, fuel_type_id) VALUES ('2.0L I4',1999, 1);
INSERT INTO engine_type (engine_type, capacity, fuel_type_id) VALUES ('V6',3600,1);
INSERT INTO engine_type (engine_type, capacity, fuel_type_id) VALUES ('Electric Motor',0, 3);


-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe');
INSERT INTO body_type (body_type) VALUES ('Hatchback');

-- Łączenie modeli z silnikami
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 1); -- Corolla (ID 1) → 2.0L I4 (ID 1)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 3); -- Corolla (ID 1) → Electric Motor (ID 3)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (2, 2); -- Mustang (ID 2) → V6 (ID 2)

--Wstawianie typów skrzyń biegów
INSERT INTO gearbox_type (gearbox_type) VALUES ('Manual');
INSERT INTO gearbox_type (gearbox_type) VALUES ('Automatic');

--Wstawianie typów napędu
INSERT INTO drive_type (drive_type) VALUES ('FWD');
INSERT INTO drive_type (drive_type) VALUES ('RWD');
INSERT INTO drive_type (drive_type) VALUES ('AWD');

-- ===============================
-- Pytania
-- ===============================
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'oil', 'oil_level_check', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'brakes', 'brake_fluid_check', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'seats', 'seat_condition', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'dashboard', 'dashboard_lights', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'engine', 'engine_noise', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'suspension', 'suspension_condition', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'brakes', 'brake_description', 'text');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'wheels', 'wheel_tread', 'number');


INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'very_low', 'oil.very_low');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'ok', 'oil.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'low', 'oil.low');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'ok', 'brake.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'low', 'brake.low');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'very_low', 'brake.very_low');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'good', 'seat.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'worn', 'seat.worn');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'damaged', 'seat.damaged');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'all_ok', 'dashboard.all_ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'warning', 'dashboard.warning');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'normal', 'engine.normal');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'abnormal', 'engine.abnormal');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'ok', 'suspension.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'noisy', 'suspension.noisy');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'damaged', 'suspension.damaged');
