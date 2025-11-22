package com.example.pruebachn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Inicializador de base de datos que crea la BD si no existe.
 * Se ejecuta como respaldo en caso de que el script de Docker no se haya ejecutado.
 */
@Component
@Order(1)
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) {
        // Este método se ejecuta después de que todos los beans estén inicializados
        // Normalmente la BD ya debería estar creada por el script de Docker,
        // pero esto sirve como respaldo
        try {
            String databaseName = "pruebachn";
            String masterUrl = datasourceUrl.replace("databaseName=" + databaseName, "databaseName=master");
            
            logger.info("Verificando si la base de datos '{}' existe...", databaseName);
            
            try (Connection connection = DriverManager.getConnection(masterUrl, username, password);
                 Statement statement = connection.createStatement()) {
                
                String checkDbQuery = String.format(
                    "SELECT COUNT(*) as count FROM sys.databases WHERE name = '%s'", 
                    databaseName
                );
                
                var resultSet = statement.executeQuery(checkDbQuery);
                boolean dbExists = resultSet.next() && resultSet.getInt("count") > 0;
                
                if (!dbExists) {
                    logger.warn("La base de datos '{}' no existe. Creándola...", databaseName);
                    String createDbQuery = String.format("CREATE DATABASE [%s]", databaseName);
                    statement.executeUpdate(createDbQuery);
                    logger.info("Base de datos '{}' creada exitosamente", databaseName);
                } else {
                    logger.info("La base de datos '{}' ya existe", databaseName);
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudo verificar/crear la base de datos: {}", e.getMessage());
            logger.debug("Error completo:", e);
        }
    }
}

