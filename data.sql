INSERT INTO coordenada (latitud, longitud) VALUES
('-34.9214', '-57.9544'),  -- La Plata
('-34.6037', '-58.3816'),  -- Buenos Aires
('-31.4201', '-64.1888'),  -- Córdoba
('-32.9468', '-60.6393'),  -- Rosario
('-34.9205', '-57.9536');  -- Coordenada para avistamiento

INSERT INTO ciudad (nombre, coordenada_id) VALUES
('La Plata', 1),
('Buenos Aires', 2),
('Córdoba', 3),
('Rosario', 4);

INSERT INTO barrio (nombre, ciudad_id) VALUES
-- La Plata
('Centro', 1),
('Tolosa', 1),
('City Bell', 1),
('Gonnet', 1),
('Villa Elisa', 1),
-- Buenos Aires
('Palermo', 2),
('Recoleta', 2),
('Belgrano', 2),
-- Córdoba
('Nueva Córdoba', 3),
('Güemes', 3),
-- Rosario
('Centro', 4),
('Pichincha', 4);

INSERT INTO ranking (nombre, puntaje) VALUES
('Novato', 0),
('Colaborador', 50),
('Experto', 150),
('Héroe de Mascotas', 300),
('Leyenda', 500);

INSERT INTO medalla (nombre) VALUES
('Primera Ayuda'),
('Buen Samaritano'),
('Guardián de Mascotas'),
('Protector Animal'),
('Leyenda Animal');

INSERT INTO usuario (nombre, apellido, email, telefono, contrasenia, condicion, es_admin, barrio_id, ranking_id) VALUES
('Admin', 'Sistema', 'admin@ttps.com', '221-000-0000', 'admin123', true, true, 1, 5),
('Juan', 'Pérez', 'juan.perez@email.com', '221-111-1111', 'pass123', true, false, 1, 2),
('María', 'González', 'maria.gonzalez@email.com', '221-222-2222', 'pass123', true, false, 2, 3),
('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '221-333-3333', 'pass123', true, false, 3, 1),
('Ana', 'Martínez', 'ana.martinez@email.com', '221-444-4444', 'pass123', true, false, 4, 2),
('Luis', 'López', 'luis.lopez@email.com', '221-555-5555', 'pass123', true, false, 5, 1),
('Laura', 'Fernández', 'laura.fernandez@email.com', '11-666-6666', 'pass123', true, false, 6, 4),
('Pedro', 'Sánchez', 'pedro.sanchez@email.com', '11-777-7777', 'pass123', false, false, 7, 1),
('Sofía', 'Ramírez', 'sofia.ramirez@email.com', '351-888-8888', 'pass123', true, false, 9, 2),
('Diego', 'Torres', 'diego.torres@email.com', '341-999-9999', 'pass123', true, false, 11, 3);

INSERT INTO mascota (nombre, tamanio, color, descripcion, estado, usuario_mascota_id) VALUES
('Firulais', 'Mediano', 'Marrón', 'Perro mestizo, muy amigable, collar rojo', 'Perdido', 2),
('Luna', 'Pequeño', 'Blanco', 'Gata persa, ojos azules, muy asustadiza', 'Perdido', 3),
('Max', 'Grande', 'Negro', 'Labrador negro, collar con placa identificatoria', 'Perdido', 4),
('Michi', 'Pequeño', 'Gris', 'Gato atigrado, mancha blanca en el pecho', 'Perdido', 5),
('Rocky', 'Mediano', 'Marrón y blanco', 'Beagle, muy juguetón, orejas largas', 'Perdido', 6),
('Toby', 'Pequeño', 'Blanco y negro', 'Perro pequeño tipo terrier', 'Encontrado', 7),
('Pelusa', 'Pequeño', 'Naranja', 'Gata naranja, muy cariñosa', 'Encontrado', 9),
('Bobby', 'Grande', 'Dorado', 'Golden Retriever, muy tranquilo', 'En Casa', 2),
('Nala', 'Mediano', 'Tricolor', 'Gata carey, muy independiente', 'En Casa', 3),
('Rex', 'Grande', 'Negro y marrón', 'Pastor Alemán, muy protector', 'En Casa', 10);

INSERT INTO publicacion (fecha, hora, estado, coordenada_id, mascota_id, usuario_id) VALUES
('2024-01-15', '2024-01-15 10:30:00', 'Perdido', 1, 1, 2),
('2024-01-20', '2024-01-20 14:45:00', 'Perdido', 2, 2, 3),
('2024-02-01', '2024-02-01 09:15:00', 'Perdido', 3, 3, 4),
('2024-02-10', '2024-02-10 16:20:00', 'Perdido', 4, 4, 5),
('2024-02-15', '2024-02-15 11:00:00', 'Perdido', 1, 5, 6),
('2024-02-20', '2024-02-20 13:30:00', 'Encontrado', 2, 6, 7),
('2024-03-01', '2024-03-01 10:00:00', 'Encontrado', 3, 7, 9);

INSERT INTO avistamiento (fecha, hora, comentario, coordenada_id, mascota_id, usuario_id) VALUES
('2024-01-16', '2024-01-16 08:00:00', 'Vi un perro similar cerca de la plaza', 5, 1, 3),
('2024-01-17', '2024-01-17 15:30:00', 'Creo haberlo visto en el parque', 5, 1, 4),
('2024-01-21', '2024-01-21 12:00:00', 'Gata blanca en el jardín de mi casa', 5, 2, 5),
('2024-01-22', '2024-01-22 18:45:00', 'La vi escondida bajo un auto', 5, 2, 6),
('2024-02-02', '2024-02-02 07:30:00', 'Perro negro grande corriendo por la calle', 5, 3, 2),
('2024-02-03', '2024-02-03 16:00:00', 'Lo vi cerca de la estación de tren', 5, 3, 7),
('2024-02-11', '2024-02-11 10:15:00', 'Gato gris en el techo de una casa', 5, 4, 3),
('2024-02-16', '2024-02-16 14:20:00', 'Beagle jugando en la plaza', 5, 5, 9);

INSERT INTO puntaje (puntos, ranking_id, usuario_puntaje_id) VALUES
(50, 2, 2),
(150, 3, 3),
(10, 1, 4),
(75, 2, 5),
(25, 1, 6),
(300, 4, 7),
(80, 2, 9),
(200, 3, 10);

-- SELECT COUNT(*) FROM usuario;
-- SELECT COUNT(*) FROM mascota;
-- SELECT COUNT(*) FROM publicacion;
-- SELECT COUNT(*) FROM avistamiento;
-- SELECT u.nombre, u.apellido, r.nombre as ranking, r.puntaje FROM usuario u JOIN ranking r ON u.ranking_id = r.ranking_id;
-- SELECT m.nombre, m.estado, u.nombre as dueño FROM mascota m JOIN usuario u ON m.usuario_mascota_id = u.usuario_id;