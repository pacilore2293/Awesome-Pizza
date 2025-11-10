INSERT INTO ingredient (id, name_ita, name_eng, created_at, updated_at) VALUES
(1,  'Pomodoro', 'Tomato', now(), now()),
(2,  'Mozzarella', 'Mozzarella cheese', now(), now()),
(3,  'Prosciutto cotto', 'Cooked ham', now(), now()),
(4,  'Funghi', 'Mushrooms', now(), now()),
(5,  'Carciofi', 'Artichokes', now(), now()),
(6,  'Olive nere', 'Black olives', now(), now()),
(7,  'Salame piccante', 'Spicy salami', now(), now()),
(8,  'Tonno', 'Tuna', now(), now()),
(9,  'Cipolla', 'Onion', now(), now()),
(10, 'Basilico', 'Basil', now(), now()),
(11, 'Peperoni', 'Bell peppers', now(), now()),
(12, 'Acciughe', 'Anchovies', now(), now()),
(13, 'Origano', 'Oregano', now(), now()),
(14, 'Rucola', 'Arugula', now(), now()),
(15, 'Grana a scaglie', 'Grana flakes', now(), now()),
(16, 'Gorgonzola', 'Gorgonzola cheese', now(), now()),
(17, 'Speck', 'Smoked ham', now(), now()),
(18, 'Salsiccia', 'Sausage', now(), now()),
(19, 'Uova', 'Eggs', now(), now()),
(20, 'Pancetta', 'Bacon', now(), now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO pizza (id, name_ita, name_eng, description_ita, description_eng, price, available, created_at, updated_at) VALUES
(1,  'Margherita', 'Margherita',
 'Classica pizza con pomodoro, mozzarella e basilico fresco.',
 'Classic pizza with tomato, mozzarella, and fresh basil.',
 6.00, true, now(), now()),

(2,  'Diavola', 'Spicy Diavola',
 'Pomodoro, mozzarella e salame piccante per chi ama il gusto deciso.',
 'Tomato, mozzarella, and spicy salami for strong flavors lovers.',
 7.50, true, now(), now()),

(3,  'Capricciosa', 'Capricciosa',
 'Pomodoro, mozzarella, prosciutto cotto, funghi, carciofi e olive nere.',
 'Tomato, mozzarella, cooked ham, mushrooms, artichokes, and black olives.',
 8.50, true, now(), now()),

(4,  'Quattro Formaggi', 'Four Cheese',
 'Mozzarella, gorgonzola, grana e formaggio fuso per un gusto cremoso.',
 'Mozzarella, gorgonzola, grana flakes and melted cheese for a creamy flavor.',
 8.00, true, now(), now()),

(5,  'Prosciutto e Funghi', 'Ham and Mushroom',
 'Pomodoro, mozzarella, prosciutto cotto e funghi: un grande classico.',
 'Tomato, mozzarella, cooked ham, and mushrooms: a true classic.',
 7.50, true, now(), now()),

(6,  'Vegetariana', 'Vegetarian',
 'Pomodoro, mozzarella, peperoni, cipolla, carciofi e rucola.',
 'Tomato, mozzarella, peppers, onion, artichokes, and arugula.',
 7.50, true, now(), now()),

(7,  'Speck e Grana', 'Speck and Grana',
 'Mozzarella, speck affumicato e scaglie di grana.',
 'Mozzarella, smoked ham, and grana flakes.',
 8.00, true, now(), now()),

(8,  'Salsiccia e Friarielli', 'Sausage and Broccoli Rabe',
 'Mozzarella, salsiccia e friarielli saltati.',
 'Mozzarella, sausage, and sautéed broccoli rabe.',
 8.50, true, now(), now()),

(9,  'Rucola e Crudo', 'Arugula and Prosciutto',
 'Mozzarella, prosciutto crudo, rucola e grana.',
 'Mozzarella, raw ham, arugula, and grana flakes.',
 8.50, true, now(), now()),

(10, 'Carbonara', 'Carbonara',
 'Mozzarella, uova, pancetta e pepe nero: ispirata alla tradizione romana.',
 'Mozzarella, eggs, bacon, and black pepper: inspired by Roman carbonara.',
 8.00, true, now(), now())
ON CONFLICT (id) DO NOTHING;

INSERT INTO pizza_ingredient (fk_pizza_id, fk_ingredient_id, quantity) VALUES
-- Margherita
(1, 1, 80.0),  -- Pomodoro
(1, 2, 100.0), -- Mozzarella
(1, 10, 2.0),  -- Basilico

-- Diavola
(2, 1, 80.0),  -- Pomodoro
(2, 2, 100.0), -- Mozzarella
(2, 7, 20.0),  -- Salame piccante

-- Capricciosa
(3, 1, 70.0),  -- Pomodoro
(3, 2, 90.0),  -- Mozzarella
(3, 3, 25.0),  -- Prosciutto cotto
(3, 4, 20.0),  -- Funghi
(3, 5, 15.0),  -- Carciofi
(3, 6, 10.0),  -- Olive nere

-- Quattro Formaggi
(4, 2, 70.0),  -- Mozzarella
(4, 15, 20.0), -- Grana a scaglie
(4, 16, 30.0), -- Gorgonzola

-- Prosciutto e Funghi
(5, 1, 80.0),  -- Pomodoro
(5, 2, 90.0),  -- Mozzarella
(5, 3, 25.0),  -- Prosciutto cotto
(5, 4, 20.0),  -- Funghi

-- Vegetariana
(6, 1, 70.0),  -- Pomodoro
(6, 2, 90.0),  -- Mozzarella
(6, 11, 25.0), -- Peperoni
(6, 9, 15.0),  -- Cipolla
(6, 5, 20.0),  -- Carciofi
(6, 14, 5.0),  -- Rucola

-- Speck e Grana
(7, 2, 90.0),  -- Mozzarella
(7, 17, 25.0), -- Speck
(7, 15, 15.0), -- Grana a scaglie

-- Salsiccia e Friarielli
(8, 2, 90.0),  -- Mozzarella
(8, 18, 35.0), -- Salsiccia

-- Rucola e Crudo
(9, 2, 90.0),  -- Mozzarella
(9, 3, 25.0),  -- Prosciutto cotto
(9, 14, 10.0), -- Rucola
(9, 15, 15.0), -- Grana a scaglie

-- Carbonara
(10, 2, 90.0), -- Mozzarella
(10, 19, 2.0), -- Uova
(10, 20, 25.0) -- Pancetta
ON CONFLICT DO NOTHING;

INSERT INTO role (id, name) VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_PIZZAIOLO')
ON CONFLICT DO NOTHING;

INSERT INTO user_auth (username, password) VALUES
('admin', '$2a$10$ohYCg1ukf6XXPQYITnHQaechYHH.6Q8HlJomtdu2Lu/ZAsxCt90wG') -- admin_pass
ON CONFLICT DO NOTHING;

INSERT INTO user_auth_role (fk_user_auth_id, fk_role_id) VALUES (1, 1)
ON CONFLICT DO NOTHING;