-- Wstawianie marek
INSERT INTO brand (brand_name) VALUES ('Toyota') ON CONFLICT DO NOTHING;; -- ID 1
INSERT INTO brand (brand_name) VALUES ('Ford') ON CONFLICT DO NOTHING;;   -- ID 2

-- Wstawianie modeli
INSERT INTO model_type (model_type, brand_id) VALUES ('Corolla', 1) ON CONFLICT DO NOTHING;; -- ID 1 (Corolla)
INSERT INTO model_type (model_type, brand_id) VALUES ('Mustang', 2) ON CONFLICT DO NOTHING;;  -- ID 2 (Mustang)

-- Wstawianie kolorów
INSERT INTO color (color) VALUES ('Red') ON CONFLICT DO NOTHING;;
INSERT INTO color (color) VALUES ('Blue') ON CONFLICT DO NOTHING;;

-- Wstawianie typów silnika
INSERT INTO engine_type (engine_type) VALUES ('V6') ON CONFLICT DO NOTHING;;       -- ID 1 (V6)
INSERT INTO engine_type (engine_type) VALUES ('Electric') ON CONFLICT DO NOTHING;; -- ID 2 (Electric)
INSERT INTO engine_type (engine_type) VALUES ('2.0L I4') ON CONFLICT DO NOTHING;;

-- Wstawianie typów nadwozia
INSERT INTO body_type (body_type) VALUES ('Coupe') ON CONFLICT DO NOTHING;;
INSERT INTO body_type (body_type) VALUES ('Hatchback') ON CONFLICT DO NOTHING;;

-- Łączenie modeli z silnikami
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 2) ON CONFLICT DO NOTHING;;
INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (1, 3) ON CONFLICT DO NOTHING;;


INSERT INTO model_engine_type (model_type_id, engine_type_id) VALUES (2, 1) ON CONFLICT DO NOTHING;;