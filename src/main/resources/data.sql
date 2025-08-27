-- Utilisateurs
INSERT INTO users (id, username, email, password, role) VALUES
(1, 'admin', 'admin@badminton.com', '$2a$10$7Q9BQeTIYfQnBQWhuWq2ceyT39hYfmnMmr6TnITVPGIhYV9dUdR8q', 'ADMIN'),
(2, 'john_doe', 'john@example.com', '$2a$10$uSj0JtS.YA8sUg3S4rPReO1bG1eQhoH5eCDLwzZ6mZ5M.b3aPjvHS', 'STANDARD'),
(3, 'jane_doe', 'jane@example.com', '$2a$10$uSj0JtS.YA8sUg3S4rPReO1bG1eQhoH5eCDLwzZ6mZ5M.b3aPjvHS', 'STANDARD');

-- ⚠️ Les mots de passe sont hashés avec BCrypt :
-- 'admin123', 'password123', 'password123'

-- Produits / Biens
INSERT INTO products (id, name, description, price, stock) VALUES
(1, 'Raquette Yonex Astrox', 'Raquette haut de gamme pour joueurs confirmés', 120.00, 10),
(2, 'Volants Yonex Mavis 350', 'Tube de 6 volants plastiques durables', 12.50, 50),
(3, 'Chaussures Victor', 'Chaussures légères et antidérapantes', 75.00, 20),
(4, 'Sac de badminton Li-Ning', 'Sac 3 compartiments pour équipement complet', 55.00, 15);

-- Réservations / Commandes
INSERT INTO reservations (id, user_id, product_id, quantity, status) VALUES
(1, 2, 1, 1, 'CONFIRMED'), -- John réserve 1 raquette
(2, 2, 2, 2, 'PENDING'),   -- John commande 2 tubes de volants
(3, 3, 3, 1, 'CONFIRMED'); -- Jane prend 1 paire de chaussures
