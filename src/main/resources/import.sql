-- Wstawianie marek (ID: 1 i 2)
INSERT INTO brand (brand_name) VALUES ('Toyota');
INSERT INTO brand (brand_name) VALUES ('Ford');

-- Wstawianie modeli (ID: 1 i 2)
INSERT INTO model_type (model_type, brand_id, basic_score) VALUES ('Corolla', 1, 65);
INSERT INTO model_type (model_type, brand_id, basic_score) VALUES ('Mustang', 2, 55);
INSERT INTO model_type (model_type, brand_id, basic_score) VALUES ('Aygo', 1, 70);


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
INSERT INTO body_type (body_type) VALUES ('Sedan');

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

--Wstawianie konkretnych modeli
INSERT INTO model_variant(model_type_id, engine_type_id, body_type_id,year_from, year_to,generation_code, facelift,basic_score,source_url, source_ratings_count, source_avg_rating) VALUES ((SELECT id FROM model_type WHERE model_type = 'Corolla'),(SELECT id FROM engine_type WHERE engine_type = '2.0L I4' AND capacity = 1999),(SELECT id FROM body_type WHERE body_type = 'Sedan'),2004, 2007,'IX', FALSE,70,'https://www.autocentrum.pl/oceny/toyota/corolla/ix-e12/sedan/silnik-diesla-2.0-d-4d-116km-2004-2007/',18,4.16);

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



INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (1, 'readable', 'vin.readable','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (1, 'mismatch', 'vin.mismatch','RED');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (1, 'not_readable', 'vin.not_readable','RED');

INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (2, 'coherent', 'service_docs.coherent','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (2, 'partial', 'service_docs.partial','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (2, 'missing', 'service_docs.missing','RED');

-- id=3 lights_working
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (3, 'ok', 'lights_working.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (3, 'single_fault', 'lights_working.single_fault','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (3, 'multiple_faults', 'lights_working.multiple_faults','RED');

-- id=4 headlights_condition
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (4, 'good', 'headlights_condition.good','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (4, 'worn', 'headlights_condition.worn','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (4, 'damaged', 'headlights_condition.damaged','RED');

-- id=5 windows_condition
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (5, 'good', 'windows_condition.good','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (5, 'chips', 'windows_condition.chips','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (5, 'cracked', 'windows_condition.cracked','RED');

-- id=6 tires_tread
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (6, 'good', 'tires_tread.good','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (6, 'borderline', 'tires_tread.borderline','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (6, 'worn', 'tires_tread.worn','RED');

-- id=7 tires_wear
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (7, 'even', 'tires_wear.even','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (7, 'uneven', 'tires_wear.uneven','RED');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (7, 'damaged', 'tires_wear.damaged','RED');

-- id=8 tires_type
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (8, 'same', 'tires_type.same','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (8, 'mixed_brands', 'tires_type.mixed_brands','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (8, 'mixed_sizes', 'tires_type.mixed_sizes','RED');

-- id=9 oil_level
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (9, 'correct', 'oil_level.correct','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (9, 'low', 'oil_level.low','RED');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (9, 'high', 'oil_level.high','RED');

-- id=10 oil_condition
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (10, 'clean', 'oil_condition.clean','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (10, 'dark', 'oil_condition.dark','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (10, 'contaminated', 'oil_condition.contaminated','RED');

-- id=11 coolant_level
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (11, 'correct', 'coolant_level.correct','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (11, 'low', 'coolant_level.low','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (11, 'leak', 'coolant_level.leak','RED');

-- id=12 visible_leaks
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (12, 'none', 'visible_leaks.none','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (12, 'minor', 'visible_leaks.minor','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (12, 'major', 'visible_leaks.major','RED');

-- id=13 upholstery_condition
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (13, 'good', 'upholstery_condition.good','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (13, 'normal', 'upholstery_condition.normal','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (13, 'damaged', 'upholstery_condition.damaged','RED');

-- id=14 steering_wear
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (14, 'good', 'steering_wear.good','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (14, 'worn', 'steering_wear.worn','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (14, 'excessive', 'steering_wear.excessive','RED');

-- id=15 interior_smell
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (15, 'none', 'interior_smell.none','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (15, 'light', 'interior_smell.light','RED');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (15, 'strong', 'interior_smell.strong','RED');

-- id=16 windows_electric
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (16, 'ok', 'windows_electric.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (16, 'slow', 'windows_electric.slow','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (16, 'not_working', 'windows_electric.not_working','RED');

-- id=17 mirrors_adjustment
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (17, 'ok', 'mirrors_adjustment.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (17, 'partial', 'mirrors_adjustment.partial','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (17, 'not_working', 'mirrors_adjustment.not_working','RED');

-- id=18 radio_function
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (18, 'ok', 'radio_function.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (18, 'distorted', 'radio_function.distorted','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (18, 'not_working', 'radio_function.not_working','RED');

-- id=19 dashboard_lights
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (19, 'ok', 'dashboard_lights.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (19, 'single', 'dashboard_lights.single','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (19, 'multiple', 'dashboard_lights.multiple','RED');

-- id=20 air_conditioning
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (20, 'ok', 'air_conditioning.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (20, 'weak', 'air_conditioning.weak','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (20, 'not_working', 'air_conditioning.not_working','RED');

-- id=21 blower_speeds
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (21, 'ok', 'blower_speeds.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (21, 'partial', 'blower_speeds.partial','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (21, 'not_working', 'blower_speeds.not_working','RED');

-- id=22 cold_start
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (22, 'ok', 'cold_start.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (22, 'long', 'cold_start.long','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (22, 'problem', 'cold_start.problem','RED');

-- id=23 idle_operation
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (23, 'stable', 'idle_operation.stable','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (23, 'unstable', 'idle_operation.unstable','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (23, 'stalling', 'idle_operation.stalling','RED');

-- id=24 exhaust_smoke
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (24, 'none', 'exhaust_smoke.none','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (24, 'temporary', 'exhaust_smoke.temporary','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (24, 'constant', 'exhaust_smoke.constant','RED');

-- id=25 clutch_operation
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (25, 'ok', 'clutch_operation.ok','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (25, 'point', 'clutch_operation.point','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (25, 'slipping', 'clutch_operation.slipping','RED');

-- id=26 gearbox_operation
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (26, 'smooth', 'gearbox_operation.smooth','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (26, 'resistance', 'gearbox_operation.resistance','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (26, 'faulty', 'gearbox_operation.faulty','RED');

-- id=27 acceleration
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (27, 'normal', 'acceleration.normal','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (27, 'weak', 'acceleration.weak','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (27, 'jerky', 'acceleration.jerky','RED');

-- id=28 steering_response
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (28, 'precise', 'steering_response.precise','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (28, 'play', 'steering_response.play','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (28, 'noises', 'steering_response.noises','RED');

-- id=29 suspension_behavior
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (29, 'quiet', 'suspension_behavior.quiet','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (29, 'minor_noise', 'suspension_behavior.minor_noise','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (29, 'loud_noise', 'suspension_behavior.loud_noise','RED');

-- id=30 brakes_operation
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (30, 'effective', 'brakes_operation.effective','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (30, 'vibrations', 'brakes_operation.vibrations','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (30, 'weak', 'brakes_operation.weak','RED');

-- id=31 driving_noises
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (31, 'none', 'driving_noises.none','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (31, 'occasional', 'driving_noises.occasional','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (31, 'constant', 'driving_noises.constant','RED');

-- id=32 vibrations
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (32, 'none', 'vibrations.none','GREEN');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (32, 'light', 'vibrations.light','YELLOW');
INSERT INTO question_option (question_id, question_option, label_key, risk_band) VALUES (32, 'strong', 'vibrations.strong','RED');

