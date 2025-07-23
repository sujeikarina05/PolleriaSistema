-- Base de datos Polloloco
CREATE DATABASE IF NOT EXISTS polloloco_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE polloloco_db;

-- Tabla: rol
CREATE TABLE rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla: usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    usuario  VARCHAR(50)  NOT NULL UNIQUE,
    clave    VARCHAR(100) NOT NULL,
    id_rol   INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id)
);

-- Tabla: mesa
CREATE TABLE mesa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero    INT  NOT NULL,
    capacidad INT  NOT NULL,
    estado ENUM('Libre', 'Ocupada', 'Reservada') DEFAULT 'Libre'
);

-- Tabla: turno
CREATE TABLE turno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50),
    hora_inicio TIME,
    hora_fin    TIME
);

-- Tabla: reserva
CREATE TABLE reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    hora  TIME,
    id_mesa    INT,
    id_usuario INT,
    nombre_cliente   VARCHAR(100),
    telefono_cliente VARCHAR(20),
    estado ENUM('Pendiente', 'Confirmada', 'Cancelada') DEFAULT 'Pendiente',
    FOREIGN KEY (id_mesa)    REFERENCES mesa(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

-- Tabla: categoria_producto
CREATE TABLE categoria_producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100),
    descripcion  TEXT
);

-- Tabla: producto
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100),
    descripcion  TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock  INT NOT NULL,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categoria_producto(id)
);

-- Tabla: metodo_pago
CREATE TABLE metodo_pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

-- Tabla: comprobante
CREATE TABLE comprobante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('Boleta', 'Factura', 'Nota de venta') NOT NULL,
    serie       VARCHAR(5)  NOT NULL,
    correlativo VARCHAR(8)  NOT NULL,
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    total    DECIMAL(10,2) NOT NULL,
    igv      DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    id_metodo_pago INT NOT NULL,
    estado ENUM('Emitido', 'Anulado', 'Pendiente') DEFAULT 'Emitido',
    observaciones TEXT,
    FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id)
);

-- Tabla: pedido
CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('Pendiente', 'En preparación', 'Entregado', 'Cancelado')
           DEFAULT 'Pendiente',
    id_mesa       INT,
    id_usuario    INT,
    id_comprobante INT,
    FOREIGN KEY (id_mesa)        REFERENCES mesa(id),
    FOREIGN KEY (id_usuario)     REFERENCES usuario(id),
    FOREIGN KEY (id_comprobante) REFERENCES comprobante(id)
);

-- Tabla: detalle_pedido
CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido   INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido)   REFERENCES pedido(id),
    FOREIGN KEY (id_producto) REFERENCES producto(id)
);

-- Tabla: configuracion
CREATE TABLE configuracion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(50) NOT NULL UNIQUE,
    valor TEXT
);

-- ---------------- DATOS INICIALES ---------------- --

-- Roles
INSERT INTO rol (nombre) VALUES
  ('Administrador'),
  ('Cajero'),
  ('Mozo');

-- Usuarios
INSERT INTO usuario (nombre, usuario, clave, id_rol) VALUES
  ('Karina Sujei', 'admin',  '1234', 1),
  ('Mario Pérez',  'cajero1','5678', 2),
  ('Rosa Rodríguez','mozo1', 'abcd', 3);
