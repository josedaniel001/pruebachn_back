-- Script de inicialización para crear la base de datos
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'pruebachn')
BEGIN
    CREATE DATABASE pruebachn;
    PRINT 'Base de datos pruebachn creada exitosamente';
END
ELSE
BEGIN
    PRINT 'La base de datos pruebachn ya existe';
END

