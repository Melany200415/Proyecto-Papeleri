CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL, 
    telefono VARCHAR(15),
    correo VARCHAR(100),
    estado TINYINT(1) DEFAULT 1,
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0,
    estado TINYINT(1) DEFAULT 1,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

	CREATE TABLE ventas (
		id_venta INT AUTO_INCREMENT PRIMARY KEY,
		fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
		subtotal DECIMAL(10,2),
		iva DECIMAL(10,2),
		total DECIMAL(10,2),
		id_usuario INT,
		FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE detalle_venta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT,
    id_producto INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

CREATE TABLE movimientos_inventario (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_usuario INT,
    tipo ENUM('entrada', 'salida'),
    cantidad INT,
    motivo VARCHAR(200),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
Select * from proveedores;

Delete from proveedores where id_proveedor = 2;

INSERT INTO usuarios (
    nombre,
    apellido,
    usuario,
    contrasena,
    telefono,
    correo,
    estado,
    id_rol
) VALUES (
    'Juan',
    'Guitierrez',
    'admin',
    'admin123',
    '0991234567',
    'admin@papeleria.com',
    1,
    1
);

INSERT INTO usuarios (
    nombre,
    apellido,
    usuario,
    contrasena,
    telefono,
    correo,
    estado,
    id_rol
) VALUES (
    'Carlos',
    'Pérez',
    'bodeguero',
    'bodega123',
    '0987654321',
    'bodeguero@papeleria.com',
    1,
    3
);

INSERT INTO productos (codigo, nombre, descripcion, precio, stock, estado, id_categoria) VALUES

('LAP001', 'Lápiz de Grafito HB', 'Lápiz de madera número 2', 0.25, 500, 1, 7),
('LAP002', 'Lapicero Azul BIC', 'Bolígrafo de tinta azul punto mediano', 0.40, 300, 1, 7),
('LAP003', 'Lapicero Negro BIC', 'Bolígrafo de tinta negra punto mediano', 0.40, 300, 1, 7),
('BOR001', 'Borrador Blanco', 'Borrador de caucho para lápiz', 0.30, 200, 1, 7),
('SAC001', 'Sacapuntas Metálico', 'Sacapuntas de un orificio', 0.35, 150, 1, 7),
('TIJ001', 'Tijera Escolar', 'Tijera punta roma', 0.90, 90, 1, 7),
('REGLA001', 'Regla 30cm', 'Regla plástica transparente', 0.30, 200, 1, 7),
('COL001', 'Caja de Colores x12', 'Lápices de colores marca Faber', 3.00, 70, 1, 7),

('PAP001', 'Cuaderno Universitario 100 hojas', 'Cuaderno rayado tamaño universitario', 1.75, 150, 1, 3),
('PAP002', 'Cuaderno Argollado 200 hojas', 'Cuaderno con espiral, cuadro grande', 3.25, 80, 1, 3),
('PAP003', 'Resma de Papel Bond A4', 'Paquete de 500 hojas tamaño A4', 4.50, 60, 1, 3),
('CARP001', 'Carpeta Manila Oficio', 'Carpeta de cartulina tamaño oficio', 0.20, 250, 1, 3),
('FOL001', 'Folder Plástico con Gancho', 'Folder tamaño carta', 0.45, 180, 1, 3),

('GRAP001', 'Grapadora Estándar', 'Grapadora de escritorio', 3.75, 40, 1, 2),
('GRAP002', 'Caja de Grapas', 'Caja de 1000 grapas estándar', 0.65, 90, 1, 2),
('CIN001', 'Cinta Adhesiva Transparente', 'Cinta de 18mm x 40m', 0.55, 100, 1, 2),
('GOM001', 'Goma en Barra', 'Pegamento en barra 21g', 0.60, 130, 1, 2),

('CLIP001', 'Caja de Clips Metálicos', 'Caja de 100 clips estándar', 0.50, 100, 1, 8),
('POST001', 'Bloc de Notas Adhesivas', 'Post-it 3x3 pulgadas', 1.20, 80, 1, 8),
('PERF001', 'Perforadora de 2 Huecos', 'Perforadora estándar de oficina', 4.00, 35, 1, 8),

('CALC001', 'Calculadora Científica', 'Calculadora Casio fx-82', 12.50, 25, 1, 4),
('USB001', 'Memoria USB 16GB', 'Unidad de almacenamiento USB 2.0', 5.00, 45, 1, 4),

('TINT001', 'Cartucho de Tinta Negra HP', 'Cartucho de impresora HP 664', 15.00, 20, 1, 10),
('MOUSE001', 'Mouse Óptico USB', 'Mouse alámbrico básico', 6.50, 30, 1, 10),

('MOCH001', 'Mochila Escolar', 'Mochila resistente con varios compartimentos', 18.00, 30, 1, 5),
('LONCH001', 'Lonchera Térmica', 'Lonchera con aislante térmico', 6.50, 40, 1, 5),

('TEM001', 'Témperas x6 colores', 'Set de pinturas témpera', 2.50, 50, 1, 11),
('MAR001', 'Marcador Permanente Negro', 'Marcador de punta gruesa', 0.85, 100, 1, 11);
Select * from usuarios;

