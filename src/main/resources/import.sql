-- Wstawianie marek (ID: 1 i 2)
INSERT INTO brand (brand_name) VALUES ('Toyota') ON CONFLICT DO NOTHING;;
INSERT INTO brand (brand_name) VALUES ('Ford') ON CONFLICT DO NOTHING;;

-- Wstawianie modeli (ID: 1 i 2)
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1) ON CONFLICT DO NOTHING;;
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2) ON CONFLICT DO NOTHING;;

-- Wstawianie kolorów (ID: 1 i 2)
INSERT INTO color (color) VALUES ('Red') ON CONFLICT DO NOTHING;;
INSERT INTO color (color) VALUES ('Blue') ON CONFLICT DO NOTHING;;

-- Wstawianie typów paliwa (ID: 1, 2 i 3)
INSERT INTO fuel_type (fuel) VALUES ('Petrol') ON CONFLICT DO NOTHING;;
INSERT INTO fuel_type (fuel) VALUES ('Diesel') ON CONFLICT DO NOTHING;;
INSERT INTO fuel_type (fuel) VALUES ('Electric') ON CONFLICT DO NOTHING;;

-- Wstawianie typów silnika z poprawnymi referencjami do paliwa
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('2.0L I4', 1) ON CONFLICT DO NOTHING;;
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('V6', 1) ON CONFLICT DO NOTHING;;
INSERT INTO engine_type (engine_type, fuel_type_id) VALUES ('Electric Motor', 3) ON CONFLICT DO NOTHING;;


-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe') ON CONFLICT DO NOTHING;;
INSERT INTO body_type (body_type) VALUES ('Hatchback') ON CONFLICT DO NOTHING;;

-- Łączenie modeli z silnikami
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 1) ON CONFLICT DO NOTHING;; -- Corolla (ID 1) → 2.0L I4 (ID 1)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 3) ON CONFLICT DO NOTHING;; -- Corolla (ID 1) → Electric Motor (ID 3)
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (2, 2) ON CONFLICT DO NOTHING;; -- Mustang (ID 2) → V6 (ID 2)

--Wstawianie typów skrzyń biegów
INSERT INTO gearbox_type (gearbox_type) VALUES ('Manual') ON CONFLICT DO NOTHING;;
INSERT INTO gearbox_type (gearbox_type) VALUES ('Automatic') ON CONFLICT DO NOTHING;;

--Wstawianie typów napędu
INSERT INTO drive_type (drive_type) VALUES ('FWD') ON CONFLICT DO NOTHING;;
INSERT INTO drive_type (drive_type) VALUES ('RWD') ON CONFLICT DO NOTHING;;
INSERT INTO drive_type (drive_type) VALUES ('AWD') ON CONFLICT DO NOTHING;;

-- ===============================
-- Pytania
-- ===============================
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'oil', 'oil_level_check', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'brakes', 'brake_fluid_check', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'seats', 'seat_condition', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'dashboard', 'dashboard_lights', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'engine', 'engine_noise', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'suspension', 'suspension_condition', 'select') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'brakes', 'brake_description', 'text') ON CONFLICT DO NOTHING;;
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'wheels', 'wheel_tread', 'number') ON CONFLICT DO NOTHING;;


INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'very_low', 'oil.very_low') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'ok', 'oil.ok') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'low', 'oil.low') ON CONFLICT DO NOTHING;;

INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'ok', 'brake.ok') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'low', 'brake.low') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'very_low', 'brake.very_low') ON CONFLICT DO NOTHING;;

INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'good', 'seat.good') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'worn', 'seat.worn') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'damaged', 'seat.damaged') ON CONFLICT DO NOTHING;;

INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'all_ok', 'dashboard.all_ok') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'warning', 'dashboard.warning') ON CONFLICT DO NOTHING;;

INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'normal', 'engine.normal') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'abnormal', 'engine.abnormal') ON CONFLICT DO NOTHING;;

INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'ok', 'suspension.ok') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'noisy', 'suspension.noisy') ON CONFLICT DO NOTHING;;
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'damaged', 'suspension.damaged') ON CONFLICT DO NOTHING;;
