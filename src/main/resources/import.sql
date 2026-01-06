-- Wstawianie marek (ID: 1 i 2)
INSERT INTO brand (brand_name) VALUES ('Toyota');
INSERT INTO brand (brand_name) VALUES ('Ford');

-- Wstawianie modeli (ID: 1 i 2)
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1);
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2);
INSERT INTO model_type (model_type, brand_id) VALUES ('Aygo', 1);

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
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (3, 3);

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

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'docs', 'vin_readable', 'select');
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'docs', 'service_docs', 'select');
-- CONSUMABLES (exterior/tires/fluids)
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'exterior', 'lights_working', 'select');          -- id=3
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'exterior', 'headlights_condition', 'select');     -- id=4
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'exterior', 'windows_condition', 'select');        -- id=5

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'tires', 'tires_tread', 'select');                -- id=6
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'tires', 'tires_wear', 'select');                 -- id=7
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'tires', 'tires_type', 'select');                 -- id=8

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'fluids', 'oil_level', 'select');                  -- id=9
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'fluids', 'oil_condition', 'select');              -- id=10
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'fluids', 'coolant_level', 'select');              -- id=11
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('consumables', 'fluids', 'visible_leaks', 'select');              -- id=12

-- INTERIOR (trim/electronics/startup)
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'trim', 'upholstery_condition', 'select');            -- id=13
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'trim', 'steering_wear', 'select');                   -- id=14
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'trim', 'interior_smell', 'select');                  -- id=15

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'electronics', 'windows_electric', 'select');          -- id=16
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'electronics', 'mirrors_adjustment', 'select');        -- id=17
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'electronics', 'radio_function', 'select');            -- id=18

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'startup', 'dashboard_lights', 'select');              -- id=19
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'startup', 'air_conditioning', 'select');              -- id=20
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('interior', 'startup', 'blower_speeds', 'select');                 -- id=21

-- MECHANICS (engine/drivetrain/roadtest)
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'engine', 'cold_start', 'select');                    -- id=22
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'engine', 'idle_operation', 'select');                -- id=23
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'engine', 'exhaust_smoke', 'select');                 -- id=24

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'drivetrain', 'clutch_operation', 'select');          -- id=25
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'drivetrain', 'gearbox_operation', 'select');         -- id=26

INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'acceleration', 'select');                -- id=27
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'steering_response', 'select');           -- id=28
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'suspension_behavior', 'select');         -- id=29
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'brakes_operation', 'select');            -- id=30
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'driving_noises', 'select');              -- id=31
INSERT INTO question (main_category, sub_category, question_key, question_type) VALUES ('mechanics', 'roadtest', 'vibrations', 'select');                  -- id=32



INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'readable', 'vin.readable');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'mismatch', 'vin.mismatch');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (1, 'not_readable', 'vin.not_readable');

INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'coherent', 'service_docs.coherent');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'partial', 'service_docs.partial');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (2, 'missing', 'service_docs.missing');

-- id=3 lights_working
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'ok', 'lights_working.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'single_fault', 'lights_working.single_fault');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (3, 'multiple_faults', 'lights_working.multiple_faults');

-- id=4 headlights_condition
INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'good', 'headlights_condition.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'worn', 'headlights_condition.worn');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (4, 'damaged', 'headlights_condition.damaged');

-- id=5 windows_condition
INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'good', 'windows_condition.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'chips', 'windows_condition.chips');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (5, 'cracked', 'windows_condition.cracked');

-- id=6 tires_tread
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'good', 'tires_tread.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'borderline', 'tires_tread.borderline');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (6, 'worn', 'tires_tread.worn');

-- id=7 tires_wear
INSERT INTO question_option (question_id, question_option, label_key) VALUES (7, 'even', 'tires_wear.even');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (7, 'uneven', 'tires_wear.uneven');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (7, 'damaged', 'tires_wear.damaged');

-- id=8 tires_type
INSERT INTO question_option (question_id, question_option, label_key) VALUES (8, 'same', 'tires_type.same');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (8, 'mixed_brands', 'tires_type.mixed_brands');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (8, 'mixed_sizes', 'tires_type.mixed_sizes');

-- id=9 oil_level
INSERT INTO question_option (question_id, question_option, label_key) VALUES (9, 'correct', 'oil_level.correct');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (9, 'low', 'oil_level.low');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (9, 'high', 'oil_level.high');

-- id=10 oil_condition
INSERT INTO question_option (question_id, question_option, label_key) VALUES (10, 'clean', 'oil_condition.clean');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (10, 'dark', 'oil_condition.dark');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (10, 'contaminated', 'oil_condition.contaminated');

-- id=11 coolant_level
INSERT INTO question_option (question_id, question_option, label_key) VALUES (11, 'correct', 'coolant_level.correct');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (11, 'low', 'coolant_level.low');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (11, 'leak', 'coolant_level.leak');

-- id=12 visible_leaks
INSERT INTO question_option (question_id, question_option, label_key) VALUES (12, 'none', 'visible_leaks.none');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (12, 'minor', 'visible_leaks.minor');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (12, 'major', 'visible_leaks.major');

-- id=13 upholstery_condition
INSERT INTO question_option (question_id, question_option, label_key) VALUES (13, 'good', 'upholstery_condition.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (13, 'normal', 'upholstery_condition.normal');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (13, 'damaged', 'upholstery_condition.damaged');

-- id=14 steering_wear
INSERT INTO question_option (question_id, question_option, label_key) VALUES (14, 'good', 'steering_wear.good');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (14, 'worn', 'steering_wear.worn');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (14, 'excessive', 'steering_wear.excessive');

-- id=15 interior_smell
INSERT INTO question_option (question_id, question_option, label_key) VALUES (15, 'none', 'interior_smell.none');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (15, 'light', 'interior_smell.light');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (15, 'strong', 'interior_smell.strong');

-- id=16 windows_electric
INSERT INTO question_option (question_id, question_option, label_key) VALUES (16, 'ok', 'windows_electric.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (16, 'slow', 'windows_electric.slow');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (16, 'not_working', 'windows_electric.not_working');

-- id=17 mirrors_adjustment
INSERT INTO question_option (question_id, question_option, label_key) VALUES (17, 'ok', 'mirrors_adjustment.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (17, 'partial', 'mirrors_adjustment.partial');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (17, 'not_working', 'mirrors_adjustment.not_working');

-- id=18 radio_function
INSERT INTO question_option (question_id, question_option, label_key) VALUES (18, 'ok', 'radio_function.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (18, 'distorted', 'radio_function.distorted');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (18, 'not_working', 'radio_function.not_working');

-- id=19 dashboard_lights
INSERT INTO question_option (question_id, question_option, label_key) VALUES (19, 'ok', 'dashboard_lights.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (19, 'single', 'dashboard_lights.single');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (19, 'multiple', 'dashboard_lights.multiple');

-- id=20 air_conditioning
INSERT INTO question_option (question_id, question_option, label_key) VALUES (20, 'ok', 'air_conditioning.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (20, 'weak', 'air_conditioning.weak');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (20, 'not_working', 'air_conditioning.not_working');

-- id=21 blower_speeds
INSERT INTO question_option (question_id, question_option, label_key) VALUES (21, 'ok', 'blower_speeds.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (21, 'partial', 'blower_speeds.partial');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (21, 'not_working', 'blower_speeds.not_working');

-- id=22 cold_start
INSERT INTO question_option (question_id, question_option, label_key) VALUES (22, 'ok', 'cold_start.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (22, 'long', 'cold_start.long');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (22, 'problem', 'cold_start.problem');

-- id=23 idle_operation
INSERT INTO question_option (question_id, question_option, label_key) VALUES (23, 'stable', 'idle_operation.stable');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (23, 'unstable', 'idle_operation.unstable');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (23, 'stalling', 'idle_operation.stalling');

-- id=24 exhaust_smoke
INSERT INTO question_option (question_id, question_option, label_key) VALUES (24, 'none', 'exhaust_smoke.none');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (24, 'temporary', 'exhaust_smoke.temporary');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (24, 'constant', 'exhaust_smoke.constant');

-- id=25 clutch_operation
INSERT INTO question_option (question_id, question_option, label_key) VALUES (25, 'ok', 'clutch_operation.ok');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (25, 'point', 'clutch_operation.point');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (25, 'slipping', 'clutch_operation.slipping');

-- id=26 gearbox_operation
INSERT INTO question_option (question_id, question_option, label_key) VALUES (26, 'smooth', 'gearbox_operation.smooth');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (26, 'resistance', 'gearbox_operation.resistance');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (26, 'faulty', 'gearbox_operation.faulty');

-- id=27 acceleration
INSERT INTO question_option (question_id, question_option, label_key) VALUES (27, 'normal', 'acceleration.normal');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (27, 'weak', 'acceleration.weak');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (27, 'jerky', 'acceleration.jerky');

-- id=28 steering_response
INSERT INTO question_option (question_id, question_option, label_key) VALUES (28, 'precise', 'steering_response.precise');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (28, 'play', 'steering_response.play');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (28, 'noises', 'steering_response.noises');

-- id=29 suspension_behavior
INSERT INTO question_option (question_id, question_option, label_key) VALUES (29, 'quiet', 'suspension_behavior.quiet');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (29, 'minor_noise', 'suspension_behavior.minor_noise');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (29, 'loud_noise', 'suspension_behavior.loud_noise');

-- id=30 brakes_operation
INSERT INTO question_option (question_id, question_option, label_key) VALUES (30, 'effective', 'brakes_operation.effective');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (30, 'vibrations', 'brakes_operation.vibrations');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (30, 'weak', 'brakes_operation.weak');

-- id=31 driving_noises
INSERT INTO question_option (question_id, question_option, label_key) VALUES (31, 'none', 'driving_noises.none');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (31, 'occasional', 'driving_noises.occasional');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (31, 'constant', 'driving_noises.constant');

-- id=32 vibrations
INSERT INTO question_option (question_id, question_option, label_key) VALUES (32, 'none', 'vibrations.none');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (32, 'light', 'vibrations.light');
INSERT INTO question_option (question_id, question_option, label_key) VALUES (32, 'strong', 'vibrations.strong');

